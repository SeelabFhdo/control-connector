/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mqttcontrol.handler;

import static org.openhab.binding.mqttcontrol.MQTTControlBindingConstants.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.smarthome.core.items.Item;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.mqttcontrol.internal.CommandConverter;
import org.openhab.binding.mqttcontrol.internal.MQTTCommand;
import org.openhab.binding.mqttcontrol.internal.MQTTControlHandlerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * The {@link MQTTControlHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Jonas Fleck - Initial contribution
 */
public class MQTTControlHandler extends BaseThingHandler implements MqttCallback {

    private Logger logger = LoggerFactory.getLogger(MQTTControlHandler.class);
    private MqttClient mqttClient;

    public MQTTControlHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        // Nothing to do here
    }

    private void reconnect() {
        Thread t1 = new Thread(() -> {
            while (true) {
                try {

                    SSLContext sslContext = SSLContext.getInstance("SSL");
                    TrustManagerFactory trustManagerFactory = TrustManagerFactory
                            .getInstance(TrustManagerFactory.getDefaultAlgorithm());
                    KeyStore keyStore = KeyStore.getInstance("JKS");
                    keyStore.load(new FileInputStream((String) getConfig().get("certPath")), "password".toCharArray());
                    trustManagerFactory.init(keyStore);
                    sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());

                    MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
                    mqttConnectOptions.setSocketFactory(sslContext.getSocketFactory());
                    mqttConnectOptions.setUserName((String) getConfig().get("username"));
                    mqttConnectOptions.setPassword(((String) getConfig().get("password")).toCharArray());

                    String instancename = (String) getConfig().get("instancename");

                    mqttClient = new MqttClient((String) getConfig().get("url"),
                            (String) getConfig().get("instancename"));
                    mqttClient.setCallback(this);
                    mqttClient.connect(mqttConnectOptions);
                    mqttClient.subscribe(MQTT_BASE_TOPIC + "/" + instancename + "/command");
                    MqttMessage message = new MqttMessage();
                    message.setPayload(instancename.getBytes());
                    mqttClient.publish(MQTT_REGISTER_TOPIC, message);
                    updateStatus(ThingStatus.ONLINE);
                    break;
                } catch (MqttException | NoSuchAlgorithmException | KeyStoreException | CertificateException
                        | IOException | KeyManagementException e) {
                    updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }
            }

        });
        t1.start();
    }

    @Override
    public void initialize() {
        updateStatus(ThingStatus.OFFLINE);
        reconnect();

        scheduler.scheduleAtFixedRate(() -> {
            try {
                String instancename = (String) getConfig().get("instancename");
                MqttMessage message = new MqttMessage();
                message.setPayload(instancename.getBytes());
                mqttClient.publish(MQTT_REGISTER_TOPIC, message);
            } catch (Exception e) {
                logger.error("error while registering at the service", e);
            }
        }, 60, 60, TimeUnit.SECONDS);

        // Note: When initialization can NOT be done set the status with more details for further
        // analysis. See also class ThingStatusDetail for all available status details.
        // Add a description to give user information to understand why thing does not work
        // as expected. E.g.
        // updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
        // "Can not access device as username and/or password are invalid");
    }

    @Override
    public void connectionLost(Throwable arg0) {
        updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR);
        reconnect();

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        logger.debug("MQTT message received");
        try {
            Gson gson = new Gson();
            MQTTCommand mCom = gson.fromJson(mqttMessage.toString(), MQTTCommand.class);
            Item i = MQTTControlHandlerFactory.getItemRegistry().getItem(mCom.getItemname());
            Command c = CommandConverter.convertCommand(mCom.getCommandtype(), mCom.getCommand());
            if (c == null) {
                logger.error("Command could not be parsed");
                return;
            }
            Method m = i.getClass().getMethod("send", c.getClass());
            m.invoke(i, c);
        } catch (Exception e) {
            logger.error("Error while parsing MQTT message", e);
        }

    }
}
