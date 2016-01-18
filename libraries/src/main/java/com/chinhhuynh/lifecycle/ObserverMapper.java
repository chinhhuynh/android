package com.chinhhuynh.lifecycle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Stores observers on specific callbacks and allows for an easy mapping between callbacks and associated observers
 */
class ObserverMapper {

    private final Class[] mSupportedCallbacks;
    /**
     * Mappings from a supported callback interface to a list of observers registered for the interface.
     */
    private final HashMap<Class<?>, List<Object>> mObserversMapping;

    public ObserverMapper(Class[] supportedCallbacks) {
        this(supportedCallbacks, new HashMap<Class<?>, List<Object>>());
    }

    public ObserverMapper(Class[] supportedCallbacks, HashMap<Class<?>, List<Object>> observersMapping) {
        mSupportedCallbacks = supportedCallbacks;
        mObserversMapping = observersMapping;
    }

    /**
     * Add an object as observer of the lifecycle. The object will receive callback for all supported lifecycle
     * callback interfaces that it implemented.
     * @param observer The object to be added as lifecycle's observer.
     */
    @SuppressWarnings("unchecked")
    public synchronized void addObserver(Object observer) {
        for (Class callbackClass : mSupportedCallbacks) {
            if (callbackClass.isAssignableFrom(observer.getClass())) {
                List<Object> callbacks = mObserversMapping.get(callbackClass);
                if (callbacks == null) {
                    callbacks = new ArrayList<>();
                    mObserversMapping.put(callbackClass, callbacks);
                }
                callbacks.add(observer);
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected synchronized <T> List<T> getObservers(Class<T> observeClass) {
        ArrayList<T> result = new ArrayList<>();
        List<T> observers = (List<T>) mObserversMapping.get(observeClass);
        if (observers != null) {
            result.addAll(observers);
        }
        return result;
    }
}
