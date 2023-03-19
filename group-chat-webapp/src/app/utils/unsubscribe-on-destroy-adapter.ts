import {Directive, OnDestroy} from '@angular/core';
import {SubscriptionLike} from "rxjs";

/**
 * A class that automatically unsubscribes all observables when the object gets destroyed
 */
@Directive()
export class UnsubscribeOnDestroyAdapter implements OnDestroy {

  /**
   * The subscriptions that holds Observable subscriptions
   */
  protected subscriptions: SubscriptionLike[] = [];

  constructor() {
  }

  /**
   * The lifecycle hook that unsubscribes all subscriptions when the component / object gets destroyed
   */
  ngOnDestroy(): void {
    this.unsubscribe();
  }

  unsubscribe() {
    this.subscriptions.forEach(sub => sub && sub.unsubscribe());
    this.subscriptions = [];
  }

  /**
   * Add subscriptions to the tracked subscriptions
   */
  addTrackedSubscriptions(...subscriptions: SubscriptionLike[]) {
    this.subscriptions = this.subscriptions.concat(subscriptions);
  }
}
