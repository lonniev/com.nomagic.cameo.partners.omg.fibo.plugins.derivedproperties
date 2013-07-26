package com.nomagic.cameo.ontology.derivedproperties.expressions.objectproperty;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nomagic.magicdraw.validation.SmartListenerConfigurationProvider;
import com.nomagic.uml2.ext.jmi.reflect.Expression;
import com.nomagic.uml2.ext.jmi.smartlistener.SmartListenerConfig;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.*;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class;
import com.nomagic.uml2.impl.PropertyNames;

import javax.annotation.CheckForNull;
import javax.jmi.reflect.RefObject;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 * User: lvanzandt
 * Date: 7/24/13
 * Time: 8:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class PropertyRangeExpression implements Expression,
        SmartListenerConfigurationProvider
{
    /**
     * Returns empty collection if the specified object is not an OWL objectProperty. If
     * specified object is an OWL objectProperty then returns the set of owlRestriction cardinalities.
     *
     * @param object the context Element from the current MD model.
     * @return collection of related OWL Restriction cardinalities.
     */
    @Override
    public Object getValue(@CheckForNull RefObject object)
    {
        List<Type> values = Lists.newArrayList();

        if (object instanceof Association) {

            Association objectProperty = (Association) object;

            // get the set of roles (MemberEnds) of this Association(Class)
            ImmutableList<Property> assocProperties = ImmutableList.copyOf(objectProperty.getMemberEnd());

            for (Property assocProperty : assocProperties) {

                // if the property can be reached from this objectProperty
                if (assocProperty.isNavigable()) {

                    // then obtain its Type because that is the range of the objectProperty
                    Type rangeType = assocProperty.getType();

                    values.add(rangeType);
                }
            }
        }

        return values;
    }

    /**
     * {@inheritDoc} An implementation of the
     * com.nomagic.magicdraw.validation.SmartListenerConfigurationProvider
     * interface has to return the configuration that identifies the set of
     * properties that are important to the expression. The derived property
     * will be recalculated when one or more of these properties change.
     */
    @Override
    public Map<java.lang.Class<? extends Element>, Collection<SmartListenerConfig>> getListenerConfigurations()
    {

        Map<java.lang.Class<? extends Element>, Collection<SmartListenerConfig>> configs =
                Maps.newHashMap();

        Collection<SmartListenerConfig> listeners = Lists.newArrayList();
        SmartListenerConfig smartListenerCfg = new SmartListenerConfig();

// if the supplier at the end of the fact-dependency, predicate-dependency changes its name
        smartListenerCfg.listenToNested(PropertyNames.CLIENT_DEPENDENCY)
                .listenTo(PropertyNames.CLIENT_DEPENDENCY)
                .listenTo(PropertyNames.SUPPLIER)
                .listenTo(PropertyNames.NAME);

        listeners.add(smartListenerCfg);

// if the supplier at the end of the fact-dependency, predicate-dependency changes its type
        smartListenerCfg.listenToNested(PropertyNames.CLIENT_DEPENDENCY)
                .listenTo(PropertyNames.CLIENT_DEPENDENCY)
                .listenTo(PropertyNames.SUPPLIER)
                .listenTo(PropertyNames.TYPE);

        listeners.add(smartListenerCfg);

// if the supplier at the end of the fact-dependency changes its Specification
        smartListenerCfg.listenToNested(PropertyNames.CLIENT_DEPENDENCY)
                .listenTo(PropertyNames.CLIENT_DEPENDENCY)
                .listenTo(PropertyNames.SUPPLIER)
                .listenTo(PropertyNames.SPECIFICATION);

        listeners.add(smartListenerCfg);

// if the client dependency at the end of the fact-dependency changes its name
        smartListenerCfg.listenToNested(PropertyNames.CLIENT_DEPENDENCY)
                .listenTo(PropertyNames.CLIENT_DEPENDENCY)
                .listenTo(PropertyNames.NAME);

        listeners.add(smartListenerCfg);

// if the client dependency changes its name
        smartListenerCfg.listenToNested(PropertyNames.CLIENT_DEPENDENCY)
                .listenTo(PropertyNames.NAME);

// if the set of client dependencies changes
        smartListenerCfg.listenTo(PropertyNames.CLIENT_DEPENDENCY);

        listeners.add(smartListenerCfg);

        configs.put(
                Class.class,
                listeners);

        return configs;
    }
}
