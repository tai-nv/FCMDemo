package DataManager;


import DTO.Schedule;

/**
 * Created by MT on 10/29/2016.
 */

public class ScheduleDataManager extends DataManager<Schedule> {
    public ScheduleDataManager() {
        super(Schedule.class);
    }

    @Override
    void addValueToDataset(Schedule value, String key) {
        value.setId(key);
        super.addValueToDataset(value,key);
    }

//    public ArrayList<Schedule> getMuscleTypeByName(String name) {
//        ArrayList<Schedule> scheduleArrayList = new ArrayList<>();
//
//        for(Schedule schedule : dataset){
//            if(schedule.getName().contains(name)){
//                scheduleArrayList.add(schedule);
//            }
//        }
//        return  scheduleArrayList;
//    }

}