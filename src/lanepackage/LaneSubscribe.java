package lanepackage;

import event.LaneEvent;
import observer.LaneObserver;
import java.util.ArrayList;
import java.util.Iterator;


public class LaneSubscribe {

    private ArrayList<LaneObserver> subscribers;

    public LaneSubscribe() {
        subscribers = new ArrayList<>();
    }

    /** subscribe
     *
     * Method that will add a subscriber
     *
     * @param subscribe	Observer that is to be added
     */

    public void subscribe( LaneObserver adding ) {
        subscribers.add( adding );
    }

    /** unsubscribe
     *
     * Method that unsubscribes an observer from this object
     *
     * @param removing	The observer to be removed
     */

    public void unsubscribe( LaneObserver removing ) {
        subscribers.remove( removing );
    }

    /** publish
     *
     * Method that publishes an event to subscribers
     *
     * @param event	Event that is to be published
     */

    public void publish( LaneEvent event ) {
        if( !subscribers.isEmpty() ) {
            Iterator<LaneObserver> eventIterator = subscribers.iterator();

            while ( eventIterator.hasNext() ) {
                (eventIterator.next()).receiveLaneEvent( event );
            }
        }
    }

}