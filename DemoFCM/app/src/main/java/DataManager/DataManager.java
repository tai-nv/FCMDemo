package DataManager;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by hungbin on 10/29/2016.
 */


public class DataManager<T> {
    public ArrayList<T> getDataset() {
        return dataset;
    }

    HashMap<String,T> firebaseHashMap = new HashMap<>();
    ArrayList<T> dataset = new ArrayList<>();
    final Class<T> mtypeParameterClass;
    static DatabaseReference referenceRoot;
    static
    {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        referenceRoot = FirebaseDatabase.getInstance().getReference();
        referenceRoot.keepSynced(true);
    }

    DatabaseReference referenceData;

    private static void copyFieldValue(Object src, Object dest, Field f) {
        try {
            Object value = f.get(src);
            f.set(dest, value);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    protected boolean conditionAddData(){
        return true;
    }

    private static void copyFields(Object src, Object dest, Class<?> klass) {
        Field[] fields = klass.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            copyFieldValue(src, dest, f);
        }

        klass = klass.getSuperclass();
        if (klass != null) {
            copyFields(src, dest, klass);
        }
    }


    public OnLoadDataComplete<T> getOnLoadDataComplete() {
        return onLoadDataComplete;
    }

    public void setOnLoadDataComplete(OnLoadDataComplete<T> onLoadDataComplete) {
        this.onLoadDataComplete = onLoadDataComplete;
    }

    OnLoadDataComplete<T> onLoadDataComplete;

    public DataManager(Class<T> typeParameterClass){
        this.mtypeParameterClass = typeParameterClass;
        String dataName = mtypeParameterClass.toString();
        dataName = dataName.substring(dataName.lastIndexOf(".")+1);
        referenceData = referenceRoot.child(dataName);


        referenceData.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if(!conditionAddData())
                    return;
                T value = (T)dataSnapshot.getValue(mtypeParameterClass);
                String key = dataSnapshot.getKey();
                addValueToDataset(value,key);


                if(onLoadDataComplete!=null) {
                    onLoadDataComplete.onComplete(value);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                T value = firebaseHashMap.get(dataSnapshot.getKey());
                T valueEdited = dataSnapshot.getValue(mtypeParameterClass);


                copyFields (valueEdited, value,mtypeParameterClass);

                if(onLoadDataComplete!=null) {
                    onLoadDataComplete.onUpdate(value);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                T value = firebaseHashMap.get(dataSnapshot.getKey());

                T object = firebaseHashMap.get(dataSnapshot.getKey());
                firebaseHashMap.remove(dataSnapshot.getKey());
                dataset.remove(object);
                if(onLoadDataComplete!=null) {
                    onLoadDataComplete.onComplete(value);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    void addValueToDataset(T value,String key)
    {
        firebaseHashMap.put(key,value);
        dataset.add(value);
    }

    public void insert(T value){

        referenceData.push().setValue(value);
    }

    public void delete(String key){
        referenceData.child(key).removeValue();

    }

    public void update(String key,T newValue){
        HashMap<String, Object> map = new HashMap<>();
        map.put(key, newValue);
        referenceData.updateChildren(map);
    }

}
