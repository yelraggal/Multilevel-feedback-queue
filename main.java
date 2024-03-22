
import java.util.Random;
import java.util.*;
public class main {
    public static void main(String[] args) {

        Queue<process> all_processes_unsorted = new LinkedList<>();//  (lts) queue is playing the role of LongtermSchandular queue (decide which process to enter to cpu)
        queue lts = new queue();
        queue comming_processes_to_system= new queue();
        queue waitting_to_EnterFirstQ = new queue(); // i make it have highpriorty than LTS queue (i see that the process already on the system have high prority than the process that didnot enter the system)
        queue waitting_to_EnterSecondQ = new queue();
        queue waitting_to_EnterthirdQ = new queue();

        int time_of_cpu = 128; //assume we will divide every 128 second of cpu
        // , (50% for first queue , 30%...,20%...)
        queue firstQ_roundroben = new queue(10,(int)(time_of_cpu*50/100));
        queue secondQ_roundroben = new queue(20,(int)(time_of_cpu*30/100));
        queue thirdQ_fcfs = new queue(30,(int)(time_of_cpu*20/100));

        Random predict = new Random();

        get_random_unique_time generate_random =new get_random_unique_time();
        // generate the 100 process and push them to lts queue
        for (int id = 1; id <= 100; id++) {
            //assume all processes arrives in random time in the first 500 seconds and no two processes arrives in the same time
            int arrival_time = generate_random.get_unique_arrrival_time();
            process p = new process(id, predict.nextInt(100) + 1,arrival_time);
            all_processes_unsorted.add(p);
            //   System.out.println("id"+p.id+ "      time   "+ p.time_remaning + " arrival time" + p.getArrival_time());
        }
        // sorted processes
        List<process> sortedProcesses = new ArrayList<>(all_processes_unsorted);
        sortedProcesses.sort(Comparator.comparingInt(process::getArrival_time));
        comming_processes_to_system.add_all_process_sorted(sortedProcesses);

        // Print the sorted list
//        System.out.println("\nSorted List based on Arrival Time:");
//        for (process p : comming_processes_to_system.my_queue) {
//            System.out.println(p);
//        }

        // start of the system
        int number_of_finshed_processes = 0;
        int system_time=0;

        while (number_of_finshed_processes != 100){
            // if the all queues are empty set the System time to the min arrival time
            if (lts.is_queue_empty()
                    && firstQ_roundroben.is_queue_empty() && secondQ_roundroben.is_queue_empty() && thirdQ_fcfs.is_queue_empty()
                    && waitting_to_EnterFirstQ.is_queue_empty() && waitting_to_EnterSecondQ.is_queue_empty() && waitting_to_EnterthirdQ.is_queue_empty()){
                system_time=comming_processes_to_system.get_first_element().arrival_time;
            }
            // lw fyh new commers hd5lhom lts bs (msh hd5lhom el first queue)
            checkForNewComers.checkForNewCommers(comming_processes_to_system,system_time,lts);

            // if there is some free spaces in first queue then push to it some process from lts or from waitingQ
            while (!firstQ_roundroben.is_queue_full() && (!lts.is_queue_empty() ||!waitting_to_EnterFirstQ.is_queue_empty())){
                if (!waitting_to_EnterFirstQ.is_queue_empty()){ // the prcess already in the system and havebeen demoted
                    // i make them have high priorty than the processes doesnot enter the system
                    waitting_to_EnterFirstQ.get_first_element().logger("-enter the first queue from waiting queue at time "+ system_time);
                    firstQ_roundroben.enqueue(waitting_to_EnterFirstQ.dequeue());
                }
                else if (!lts.is_queue_empty()){
                    lts.get_first_element().logger("-enter the first queue from the long term schadular queue at time " + system_time);
                    firstQ_roundroben.enqueue(lts.dequeue());
                }
            }

            // Now first queue will take time on cpu
            int time_taken_by_firstQ_on_cpu_tillnow = 0;
            //if there is any element we will do a roundroben with Q=8 with each element till its time on cpu ends
            while (!firstQ_roundroben.is_queue_empty() && time_taken_by_firstQ_on_cpu_tillnow < firstQ_roundroben.getTime_avalible_on_cpu()){
                process p = firstQ_roundroben.dequeue();
                if (p.get_time()<=8){
                    time_taken_by_firstQ_on_cpu_tillnow+=p.get_time();
                    p.logger("-excute "+p.get_time()+" sec on the first Queue from "+system_time+" till "+ (system_time+p.get_time()));
                    system_time+=p.get_time();
                    number_of_finshed_processes ++;
                    p.i_am_done(1);

                }else{
                    time_taken_by_firstQ_on_cpu_tillnow+=8;
                    p.logger("-excute "+"8"+" sec on the first Queue from "+system_time+" till "+(system_time+8)+" and enter the waiting second queue");
                    system_time+=8;
                    p.set_time(p.get_time()-8);
                    waitting_to_EnterSecondQ.enqueue(p);
                }
                // to handel the case that the queue still have time on cpu ,
                // and queue is free from process , so each time w dequeue a process we check for process in lts or in waiting queue
                checkForNewComers.checkForNewCommers(comming_processes_to_system,system_time,lts);
                if (!waitting_to_EnterFirstQ.is_queue_empty()){
                    waitting_to_EnterFirstQ.get_first_element().logger("-enter the first queue from watting queue at time "+system_time);
                    firstQ_roundroben.enqueue(waitting_to_EnterFirstQ.dequeue());
                }
                else if (!lts.is_queue_empty()){
                    lts.get_first_element().logger("-enter the first queue from lts at time "+system_time );
                    firstQ_roundroben.enqueue(lts.dequeue());
                }
            }

            // if the first queue empty then go to see the second queue
            if (firstQ_roundroben.is_queue_empty()){
                // Now check if there is some empty spaces in second queue
                while (!secondQ_roundroben.is_queue_full() && !waitting_to_EnterSecondQ.is_queue_empty()) {
                    // push to it from waiting to enter QUEUE 2
                    if (!waitting_to_EnterSecondQ.is_queue_empty()) {
                        waitting_to_EnterSecondQ.get_first_element().logger("-enter the second queue from the second waiting queue at time "+system_time);
                        secondQ_roundroben.enqueue(waitting_to_EnterSecondQ.dequeue());
                    }
                }
                // Now the second Queue will take time on CPU
                int time_taken_by_secondQ_on_cpu_tillnow = 0;
                while (!secondQ_roundroben.is_queue_empty() && time_taken_by_secondQ_on_cpu_tillnow < secondQ_roundroben.getTime_avalible_on_cpu()){
                    process p = secondQ_roundroben.dequeue();
                    if (p.get_time()<=16){
                        time_taken_by_secondQ_on_cpu_tillnow+=p.get_time();
                        p.logger("-execute "+p.get_time()+" sec on the second queue from "+system_time+" till "+(system_time+p.get_time()));
                        system_time+=p.get_time();
                        p.i_am_done(2);
                        number_of_finshed_processes++;
                    }else{// make it waiting to enter third QUEUE or demote it to be waiting to enter the first QUEUE
                        time_taken_by_secondQ_on_cpu_tillnow+=16;
                        // now we have to demote it or promote it
                        double rand = predict.nextDouble(); // generate a random number between 0 and 1
                        if (rand < 0.5){ //demote it حنزلها ل كيو 1
                            p.logger("-execute 16 sec on the second queue "+"from " +system_time+" till "+(system_time+16)+"and demoted to first queue ");
                            waitting_to_EnterFirstQ.enqueue(p);
                        }else{// promote it حنرئيها لكيو 3 بس بعد منرنها علي كيو 2
                            p.logger("-execute 16 sec on the second queue "+"from " +system_time+" till "+(system_time+16)+"and prmoted to the third waiting queue ");
                            waitting_to_EnterthirdQ.enqueue(p);
                        }
                        system_time+=16;
                        p.set_time(p.get_time()-16);
                    }
                    // to handel the case that Queue 2 is empty and it still have time on cpu
                    // every time we deqeue an element we insert another element if some one is waiting
                    if (!waitting_to_EnterSecondQ.is_queue_empty()) {
                        waitting_to_EnterSecondQ.get_first_element().logger("-enter the second queue from the second waiting queue at time"+system_time);
                        secondQ_roundroben.enqueue(waitting_to_EnterSecondQ.dequeue());
                    }
                    // 3shan lw fyh elemnts wslt l first hwa msh hyd5ol f third (fkk mn comment da)
                    // hwa str gy mlhosh lzma bs hsebo akni shghal tread showya , // every time we change system time we check for new arrivals
                    checkForNewComers.checkForNewCommers(comming_processes_to_system,system_time,lts);
                }
            }
            // if the first and second queue are empty then check the third queue
            if (firstQ_roundroben.is_queue_empty() && secondQ_roundroben.is_queue_empty()){
                // check if there is some free spaces on third queue then push to it some process from waiting Q
                while (!thirdQ_fcfs.is_queue_full() && !waitting_to_EnterthirdQ.is_queue_empty()){
                    if (!waitting_to_EnterthirdQ.is_queue_empty()){
                        waitting_to_EnterthirdQ.get_first_element().logger("-enter the third queue at time "+system_time);
                        thirdQ_fcfs.enqueue(waitting_to_EnterthirdQ.dequeue());
                    }
                }
                //Fcfs queue is on cpu
                int time_taken_by_thirdQ_on_cpu_tillnow = 0;
                while (!thirdQ_fcfs.is_queue_empty() && time_taken_by_thirdQ_on_cpu_tillnow < thirdQ_fcfs.getTime_avalible_on_cpu()){
                    process p = thirdQ_fcfs.dequeue(); // tl3tha bra el system
                    p.logger("-excute "+p.get_time()+" on the third queue from "+system_time +" till "+(system_time+p.get_time()));
                    p.i_am_done(3);
                    number_of_finshed_processes++;
                    time_taken_by_thirdQ_on_cpu_tillnow += p.get_time();
                    system_time+=p.get_time();
                    // to handel the case that Queue 3 is empty and it still have time on cpu
                    // every time we deqeue an element we insert another element if some one is waiting
                    if (!waitting_to_EnterthirdQ.is_queue_empty()){
                        p.logger("-enter the third queue at time "+system_time);
                        thirdQ_fcfs.enqueue(waitting_to_EnterthirdQ.dequeue());
                    }
                    // every time we change system time we check for new arrivals , akni shgal thread
                    checkForNewComers.checkForNewCommers(comming_processes_to_system,system_time,lts);
                }
            }
            //        System.out.println("NO FINISHED PROCESSES    "+ number_of_finshed_processes);
        }
        //     System.out.println(system_time);
    }
}
