import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class get_random_unique_time {
    private Set<Integer> arrivalTimes ;
    public get_random_unique_time() {
        arrivalTimes = new HashSet<>();
    }
    public   int get_unique_arrrival_time(){
        int newArrivalTime;
        do {
            newArrivalTime=new Random().nextInt(500)+1;
        }while (arrivalTimes.contains(newArrivalTime));
        arrivalTimes.add(newArrivalTime);
        return newArrivalTime;
    }
}
