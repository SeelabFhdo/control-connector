/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mqttcontrol.internal;

import static org.openhab.binding.mqttcontrol.MQTTControlBindingConstants.THING_TYPE_BROKER;

import java.util.Collections;
import java.util.Set;

import org.eclipse.smarthome.core.items.ItemRegistry;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.openhab.binding.mqttcontrol.handler.MQTTControlHandler;

/**
 * The {@link MQTTControlHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Jonas Fleck - Initial contribution
 */
public class MQTTControlHandlerFactory extends BaseThingHandlerFactory {

    private static ItemRegistry itemregistry;

    private final static Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Collections.singleton(THING_TYPE_BROKER);

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    public void setItemRegistry(ItemRegistry itemregistry) {
        MQTTControlHandlerFactory.itemregistry = itemregistry;
    }

    public static ItemRegistry getItemRegistry() {
        return itemregistry;
    }

    @Override
    protected ThingHandler createHandler(Thing thing) {

        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (thingTypeUID.equals(THING_TYPE_BROKER)) {
            return new MQTTControlHandler(thing);
        }

        return null;
    }
}
