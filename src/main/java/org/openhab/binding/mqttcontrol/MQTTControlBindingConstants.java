/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mqttcontrol;

import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The {@link MQTTControlBinding} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Jonas Fleck - Initial contribution
 */
public class MQTTControlBindingConstants {

    public static final String BINDING_ID = "mqttcontrol";

    // List of all Thing Type UIDs
    public final static ThingTypeUID THING_TYPE_BROKER = new ThingTypeUID(BINDING_ID, "broker");

    // MQTT Constants
    public final static String MQTT_BASE_TOPIC = "/controlservice";
    public final static String MQTT_REGISTER_TOPIC = MQTT_BASE_TOPIC + "/register";

}
