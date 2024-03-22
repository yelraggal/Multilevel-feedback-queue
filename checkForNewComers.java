public class checkForNewComers {
    public static void checkForNewCommers(queue comming_processes_to_system,int system_time,queue lts) {
        // check if there is new arrivals push them to lts (LONG TERM SCHADULAR) bs msh hd5lha 1st queue
        // hya htb2a td5ol el first queue lma el cpu yedi w2t bta3o l QUEUE 1
        while (!comming_processes_to_system.is_queue_empty()) {
            if (comming_processes_to_system.get_first_element().getArrival_time() <= system_time) {
                lts.enqueue(comming_processes_to_system.dequeue());
            }
            else{
                break;
            }
        }
    }
}
