package controller;

import event.ControlDeskEvent;
import java.util.*;
import observer.ControlDeskObserver;
public class ControlDeskSubscribe {
    /** The collection of subscribers */
    private ArrayList<ControlDeskObserver> subscribers;
    public ControlDeskSubscribe(){
        subscribers = new ArrayList();
    }
    /**
     * Allows objects to subscribe as observers
     *
     * @param adding	the ControlDeskObserver that will be subscribed
     *
     */

    public void subscribe(ControlDeskObserver adding) {
        subscribers.add(adding);
    }

    /**
     * Broadcast an event to subscribing objects.
     *
     * @param event	the ControlDeskEvent to broadcast
     *
     */

    public void publish(ControlDeskEvent event) {
        Iterator eventIterator = subscribers.iterator();
        while (eventIterator.hasNext()) {
            (
                    (ControlDeskObserver) eventIterator
                            .next())
                    .receiveControlDeskEvent(
                            event);
        }
    }
}
