package com.roi.greenberg.michayavlemi.utils;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

/**
 * Created by Roi on 05/08/2018.
 */
public class DatabaseHandler {
    private static final DatabaseHandler ourInstance = new DatabaseHandler();

    private FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();

    public static DatabaseHandler getInstance() {
        return ourInstance;
    }

    private DatabaseHandler() {
    }

    public Task<String> calculateTransactions(String eventId) {
        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        data.put("eventId", eventId);

        return mFunctions
                .getHttpsCallable("calculateTransactions")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        String result = (String) task.getResult().getData();
                        return result;
                    }
                });
    }


}
