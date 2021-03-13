//This file was generated from (Academic) UPPAAL 4.1.24 (rev. 29A3ECA4E5FB0808), November 2019

/*

*/
E<> (RoadUser1.RU_away_from_RJ && RoadUser2.RU_away_from_RJ && RoadUser3.RU_away_from_RJ) imply AV.check_for_safe_gap

/*

*/
A<> ((RoadUser1.RU_crossing_RJ || RoadUser2.RU_crossing_RJ || RoadUser3.RU_crossing_RJ) && RoadJunction.busy_RJ) imply AV.waiting

/*

*/
A<> (RoadUser1.RU_crossing_RJ || RoadUser2.RU_crossing_RJ || RoadUser3.RU_crossing_RJ) imply RoadJunction.busy_RJ

/*

*/
E<> (RoadUser1.RU_away_from_RJ || RoadUser2.RU_away_from_RJ) imply AV.check_for_safe_gap

/*

*/
E<> (RoadUser1.RU_away_from_RJ && RoadUser2.RU_away_from_RJ) imply AV.check_for_safe_gap

/*

*/
A<> ((RoadUser1.RU_crossing_RJ || RoadUser2.RU_crossing_RJ) && RoadJunction.busy_RJ) imply AV.waiting

/*

*/
A<> (RoadUser1.RU_crossing_RJ && RoadUser2.RU_crossing_RJ) imply RoadJunction.busy_RJ

/*

*/
A<> (RoadUser1.RU_crossing_RJ || RoadUser2.RU_crossing_RJ) imply RoadJunction.busy_RJ

/*

*/
A<> AV.AV_at_RJ imply (RoadJunction.send_stop || RoadJunction.send_stop_and_give_away)

/*

*/
A<> AV.AV_at_RJ imply RoadJunction.AV_may_enter

/*

*/
E<> RoadUser1.RU_away_from_RJ imply AV.check_for_safe_gap

/*

*/
A<> (RoadUser1.RU_crossing_RJ && RoadJunction.busy_RJ) imply AV.waiting

/*

*/
A<> RoadUser1.RU_crossing_RJ imply AV.waiting

/*

*/
A<> RoadJunction.busy_RJ imply AV.waiting

/*

*/
E<> RoadJunction.free_RJ imply AV.AV_entered_RJ

/*

*/
A[] not deadlock

/*

*/
E<> RoadJunction.AV_may_enter && AV.AV_entered_RJ

/*

*/
A[] AV.watch_out_for_RU imply (AV.x >= 0 && AV.x <= 3)

/*

*/
A[] AV.waiting imply (AV.x >= 1 && AV.x <= 5)

/*

*/
A[] AV.check_for_safe_gap imply (AV.x >= 2 && AV.x <= 4)

/*

*/
E[] AV.x >= 3 imply AV.AV_entered_RJ  

/*

*/
A<> RoadUser1.RU_crossing_RJ imply RoadJunction.busy_RJ
