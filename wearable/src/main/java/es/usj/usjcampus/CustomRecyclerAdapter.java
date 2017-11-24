/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package es.usj.usjcampus;

import android.support.v4.app.NotificationCompat;
import android.support.wearable.view.WearableRecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Provides a binding from {@link NotificationCompat.Style} data set to views displayed within the
 * {@link WearableRecyclerView}.
 */
public class CustomRecyclerAdapter extends
        WearableRecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder> {

    private static final String TAG = "CustomRecyclerAdapter";

    private Controller mDataSet;


    /**
     * Provides reference to the views for each data item. We don't maintain a reference to the
     * {@link ImageView} (representing the icon), because it does not change for each item. We
     * wanted to keep the sample simple, but you could add extra code to customize each icon.
     */
    public static class ViewHolder extends WearableRecyclerView.ViewHolder {

        public ViewHolder(View view) {
            super(view);
        }

    }

    public CustomRecyclerAdapter(Controller dataSet) {
        mDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.accept_deny_dialog, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");
    }

    @Override
    public int getItemCount() {
        return 0;
    }

}