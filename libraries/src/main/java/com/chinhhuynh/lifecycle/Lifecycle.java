package com.chinhhuynh.lifecycle;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.List;

import com.chinhhuynh.lifecycle.fragment.OnActivityCreated;
import com.chinhhuynh.lifecycle.fragment.OnAttach;
import com.chinhhuynh.lifecycle.fragment.OnCreate;
import com.chinhhuynh.lifecycle.fragment.OnDestroy;
import com.chinhhuynh.lifecycle.fragment.OnDestroyView;
import com.chinhhuynh.lifecycle.fragment.OnDetach;
import com.chinhhuynh.lifecycle.fragment.OnPause;
import com.chinhhuynh.lifecycle.fragment.OnRestart;
import com.chinhhuynh.lifecycle.fragment.OnResume;
import com.chinhhuynh.lifecycle.fragment.OnStart;
import com.chinhhuynh.lifecycle.fragment.OnStop;

/**
 * An implementation that allows an object to register itself to listen to {@link Fragment}'s lifecycle
 * callbacks.
 *
 * An object that wants to receive callback(s) from an object that has a lifecycle needs to implement corresponding
 * callback interface(s) and register itself as an observer of the fragment's lifecycle. When a supported event happens,
 * the lifecycle will forward the callback to the registered observers.
 *
 * This pattern allows developers to break down implementations into testable mixins, each able to react only to
 * certain areas of a lifecycle.
 *
 * Sample usage:
 * public class MyMixin implements OnAttach {
 *
 *   private EditText mTextInput;
 *
 *   public MyMixin(Fragment fragment, Lifecycle lifecycle) {
 *     // registers this class to listen to fragment's callbacks. Because MyMixin only implements OnAttach
 *     // interface, only OnAttach#onAttach(Activity) will be forwarded to this object.
 *     lifecycle.addObserver(this);
 *
 *     // find views in the fragment.
 *     mTextInput = (EditText) fragment.findViewById(R.id.text_input);
 *   }
 *
 *   @Override
 *   public void onAttach(Activity activity) {
 *     // logic to execute when the fragment is attached to an activity.
 *   }
 * }
 */
public class Lifecycle {

    /**
     * The list of callback interfaces supported by the {@link Lifecycle}.
     */
    private static final Class[] SUPPORTED_CALLBACKS = {
            // fragment's lifecycle callbacks
            OnAttach.class,
            OnCreate.class,
            OnActivityCreated.class,
            OnStart.class,
            OnRestart.class,
            OnResume.class,
            OnPause.class,
            OnStop.class,
            OnDestroyView.class,
            OnDestroy.class,
            OnDetach.class,
    };
    private final ObserverMapper mObserverMapper;

    public Lifecycle() {
        this(new ObserverMapper(SUPPORTED_CALLBACKS));
    }

    private Lifecycle(ObserverMapper observerMapper) {
        mObserverMapper = observerMapper;
    }

    public void addObserver(Object observer) {
        mObserverMapper.addObserver(observer);
    }

    public void onCreate(Bundle savedInstanceState) {
        List<OnCreate> observers = mObserverMapper.getObservers(OnCreate.class);
        for (OnCreate observer : observers) {
            observer.onCreate(savedInstanceState);
        }
    }

    public void onAttach(Activity activity) {
        List<OnAttach> observers = mObserverMapper.getObservers(OnAttach.class);
        for (OnAttach observer : observers) {
            observer.onAttach(activity);
        }
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        List<OnActivityCreated> observers = mObserverMapper.getObservers(OnActivityCreated.class);
        for (OnActivityCreated observer : observers) {
            observer.onActivityCreated(savedInstanceState);
        }
    }

    public void onRestart() {
        List<OnRestart> observers = mObserverMapper.getObservers(OnRestart.class);
        for (OnRestart observer : observers) {
            observer.onRestart();
        }
    }

    public void onStart() {
        List<OnStart> observers = mObserverMapper.getObservers(OnStart.class);
        for (OnStart observer : observers) {
            observer.onStart();
        }
    }

    public void onResume() {
        List<OnResume> observers = mObserverMapper.getObservers(OnResume.class);
        for (OnResume observer : observers) {
            observer.onResume();
        }
    }

    public void onPause() {
        List<OnPause> observers = mObserverMapper.getObservers(OnPause.class);
        for (OnPause observer : observers) {
            observer.onPause();
        }
    }

    public void onStop() {
        List<OnStop> observers = mObserverMapper.getObservers(OnStop.class);
        for (OnStop observer : observers) {
            observer.onStop();
        }
    }

    public void onDestroyView() {
        List<OnDestroyView> observers = mObserverMapper.getObservers(OnDestroyView.class);
        for (OnDestroyView observer : observers) {
            observer.onDestroyView();
        }
    }

    public void onDestroy() {
        List<OnDestroy> observers = mObserverMapper.getObservers(OnDestroy.class);
        for (OnDestroy observer : observers) {
            observer.onDestroy();
        }
    }

    public void onDetach() {
        List<OnDetach> observers = mObserverMapper.getObservers(OnDetach.class);
        for (OnDetach observer : observers) {
            observer.onDetach();
        }
    }
}
