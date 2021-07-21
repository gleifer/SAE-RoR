//This file was generated from (Academic) UPPAAL 4.1.24 (rev. 29A3ECA4E5FB0808), November 2019

/*

*/
RoadJunction.waiting_for_AV --> AV.watch_out_for_RU

/*

*/
RoadJunction.AV_may_enter --> AV.AV_entered_RJ

/*

*/
A[] (RoadJunction.waiting_for_AV imply AV.watch_out_for_RU)

/*

*/
A[] ((RoadUser1.RU_crossing_RJ || RoadUser2.RU_crossing_RJ || RoadUser3.RU_crossing_RJ) imply (RoadJunction.is_RJ_free || RoadJunction.busy_RJ || RoadJunction.AV_should_wait || RoadJunction.AV_is_waiting || RoadJunction.check_RU))

/*

*/
A[] ((RoadUser1.RU_crossing_RJ || RoadUser2.RU_crossing_RJ) imply (RoadJunction.is_RJ_free || RoadJunction.busy_RJ || RoadJunction.AV_should_wait || RoadJunction.AV_is_waiting || RoadJunction.check_RU))

/*

*/
A[] (RoadUser1.RU_crossing_RJ imply (RoadJunction.is_RJ_free || RoadJunction.busy_RJ || RoadJunction.AV_should_wait || RoadJunction.AV_is_waiting || RoadJunction.check_RU))

/*

*/
A[] ((AV.watch_out_for_RU) imply ((RoadUser1.RU_crossing_RJ || RoadUser1.RU_away_from_RJ) || (RoadUser2.RU_crossing_RJ || RoadUser2.RU_away_from_RJ) || (RoadUser3.RU_crossing_RJ || RoadUser3.RU_away_from_RJ)))

/*

*/
A[] ((AV.watch_out_for_RU) imply ((RoadUser1.RU_crossing_RJ || RoadUser1.RU_away_from_RJ) || (RoadUser2.RU_crossing_RJ || RoadUser2.RU_away_from_RJ)))

/*

*/
A[] ((AV.watch_out_for_RU) imply (RoadUser1.RU_crossing_RJ || RoadUser1.RU_away_from_RJ))

/*

*/
A[] ((RoadUser1.RU_crossing_RJ || RoadUser2.RU_crossing_RJ || RoadUser3.RU_crossing_RJ) imply (not AV.AV_entered_RJ))

/*

*/
A[] ((RoadUser1.RU_crossing_RJ || RoadUser2.RU_crossing_RJ) imply (not AV.AV_entered_RJ))

/*

*/
A[] (RoadUser1.RU_crossing_RJ imply (not AV.AV_entered_RJ))

/*

*/
A[] ((AV.AV_check_for_safe_gap) imply (AV.x >= 2 && AV.x <= 4))

/*

*/
A[] ((AV.waiting) imply (AV.x >= 1 && AV.x <= 5))

/*

*/
A[] ((AV.watch_out_for_RU) imply (AV.x >= 0 && AV.x <= 3))

/*

*/
A[] (AV.AV_entered_RJ imply AV.x >= 2)

/*

*/
A[] ((RoadJunction.send_stop || RoadJunction.send_stop_and_give_way) imply AV.AV_at_RJ)

/*

*/
A[] (RoadJunction.check_for_sign imply AV.AV_at_RJ)

/*

*/
A[] (AV.AV_check_for_safe_gap imply (RoadJunction.check_for_safe_gap || RoadJunction.there_is_safe_gap || RoadJunction.there_is_no_safe_gap))

/*

*/
A[] (RoadJunction.AV_may_enter imply AV.AV_entered_RJ)

/*

*/
E<> (RoadUser1.RU_away_from_RJ && RoadUser2.RU_away_from_RJ && RoadUser3.RU_away_from_RJ) == AV.check_for_safe_gap\


/*

*/
E<> (RoadUser1.RU_away_from_RJ && RoadUser2.RU_away_from_RJ) == AV.check_for_safe_gap\


/*

*/
A<> (RoadUser1.RU_crossing_RJ || RoadUser2.RU_crossing_RJ || RoadUser3.RU_crossing_RJ) == AV.waiting

/*

*/
A<> (RoadUser1.RU_crossing_RJ || RoadUser2.RU_crossing_RJ || RoadUser3.RU_crossing_RJ) == RoadJunction.busy_RJ

/*

*/
A<> (RoadUser1.RU_crossing_RJ || RoadUser2.RU_crossing_RJ) == AV.waiting

/*

*/
A<> (RoadUser1.RU_crossing_RJ || RoadUser2.RU_crossing_RJ) == RoadJunction.busy_RJ

/*

*/
AV.x >= 3 --> AV.AV_entered_RJ

/*

*/
A<> AV.check_for_safe_gap == (RoadJunction.there_is_safe_gap || RoadJunction.there_is_no_safe_gap)

/*

*/
A<> RoadJunction.AV_may_enter == AV.AV_entered_RJ

/*

*/
A<> AV.AV_at_RJ == (RoadJunction.send_stop || RoadJunction.send_stop_and_give_away)

/*

*/
A<> AV.AV_at_RJ == RoadJunction.check_for_sign

/*

*/
E<> RoadUser1.RU_away_from_RJ == AV.check_for_safe_gap\


/*

*/
A<> RoadUser1.RU_crossing_RJ == AV.waiting

/*

*/
A<> RoadUser1.RU_crossing_RJ == RoadJunction.busy_RJ

/*

*/
AV.AV_at_RJ --> AV.watch_out_for_RU

/*

*/
A<> AV.AV_entered_RJ

/*

*/
E<> (RoadUser1.RU_away_from_RJ && RoadUser2.RU_away_from_RJ && RoadUser3.RU_away_from_RJ) && AV.check_for_safe_gap\


/*

*/
E<> (RoadUser1.RU_crossing_RJ || RoadUser2.RU_crossing_RJ || RoadUser3.RU_crossing_RJ) && AV.waiting\


/*

*/
E<> (RoadUser1.RU_crossing_RJ || RoadUser2.RU_crossing_RJ || RoadUser3.RU_crossing_RJ) && RoadJunction.busy_RJ\


/*

*/
E<> (RoadUser1.RU_away_from_RJ && RoadUser2.RU_away_from_RJ) && AV.check_for_safe_gap\


/*

*/
E<> (RoadUser1.RU_crossing_RJ || RoadUser2.RU_crossing_RJ) && AV.waiting\


/*

*/
E<> (RoadUser1.RU_crossing_RJ || RoadUser2.RU_crossing_RJ) && RoadJunction.busy_RJ\


/*

*/
E<> RoadUser1.RU_away_from_RJ && AV.check_for_safe_gap\


/*

*/
E<> RoadUser1.RU_crossing_RJ && AV.waiting

/*

*/
E<> RoadUser1.RU_crossing_RJ && RoadJunction.busy_RJ

/*

*/
E<> (AV.check_for_safe_gap) && (AV.x >= 2 && AV.x <= 4)

/*

*/
E<> (AV.waiting) && (AV.x >= 1 && AV.x <= 5)

/*

*/
E<> (AV.watch_out_for_RU) && (AV.x >= 0 && AV.x <= 3)

/*

*/
E<> AV.x >= 3 && AV.AV_entered_RJ

/*

*/
E<> AV.AV_at_RJ && (RoadJunction.send_stop || RoadJunction.send_stop_and_give_away)

/*

*/
E<> (AV.check_for_safe_gap && RoadJunction.there_is_safe_gap)

/*

*/
E<> AV.AV_at_RJ && RoadJunction.check_for_sign

/*

*/
E<> RoadJunction.AV_may_enter && AV.AV_entered_RJ

/*

*/
A[] not deadlock
