package com.johnhellbom.dysseappen;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by John Hellbom on 2016-02-28.
 */
public class CarouselEntry extends Fragment {

    public static CarouselEntry newInstance(CarouselEntryData data)
    {
        CarouselEntry f = new CarouselEntry();
        Bundle bdl = new Bundle();
        bdl.putString("body", data.body);
        bdl.putString("action", data.action);
        bdl.putString("data", data.data);
        bdl.putString("imageData", data.image);
        bdl.putString("category", data.category);
        bdl.putString("categoryColor", data.categoryColor);
        bdl.putFloat("categorySize", data.categorySize);
        bdl.putString("title", data.title);
        bdl.putString("titleColor", data.titleColor);
        bdl.putFloat("titleSize", data.titleSize);
        bdl.putString("description", data.description);
        bdl.putString("descriptionColor", data.descriptionColor);
        bdl.putFloat("descriptionSize", data.descriptionSize);
        f.setArguments(bdl);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.entry_carousel, container, false);

        ImageView carouselImage = (ImageView)rootView.findViewById(R.id.carouselImage);

        String bgUrl = getArguments().getString("imageData");
        new DownloadImageTask(carouselImage).execute(bgUrl);
        carouselImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

        TextView carouselCategory = (TextView)rootView.findViewById(R.id.carouselCategory);
        TextView carouselTitle = (TextView)rootView.findViewById(R.id.carouselTitle);
        TextView carouselDescription = (TextView)rootView.findViewById(R.id.carouselDescription);

        String cat = getArguments().getString("category");
        if(cat != null) {
            carouselCategory.setText(cat.toUpperCase());
        }
        try {
            carouselCategory.setTextColor(Color.parseColor(getArguments().getString("categoryColor")));
        } catch(Exception e) {
            Log.d("CategoryColorError", "Could not set category color. (value: " + getArguments().getString("categoryColor") + ")");
        }
        carouselCategory.setTextSize(getArguments().getFloat("categorySize"));

        carouselTitle.setText(getArguments().getString("title"));
        try {
            carouselTitle.setTextColor(Color.parseColor(getArguments().getString("titleColor")));
        } catch(Exception e) {
            Log.d("TitleColorError", "Could not set title color. (value: " + getArguments().getString("titleColor") + ")");
        }
        carouselTitle.setTextSize(getArguments().getFloat("titleSize"));

        carouselDescription.setText(getArguments().getString("description"));
        try {
            carouselDescription.setTextColor(Color.parseColor(getArguments().getString("descriptionColor")));
        } catch(Exception e) {
            Log.d("DesscriptionColorError", "Could not set description color. (value: " + getArguments().getString("descriptionColor") + ")");
        }
        carouselDescription.setTextSize(getArguments().getFloat("descriptionSize"));

        RelativeLayout layout = (RelativeLayout)rootView.findViewById(R.id.content);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getActionMasked() == MotionEvent.ACTION_UP) {

                    Boolean defaultBehavior = false;

                    String action = getArguments().getString("action");
                    String data = getArguments().getString("data");
                    String title = getArguments().getString("title");
                    String body = getArguments().getString("body");

                    Intent intent;

                    assert action != null;
                    switch(action)
                    {
                        case "podcast":
                            intent = new Intent(getContext(), DyssePodden.class);
                            intent.putExtra("newsCallerData", data);
                            startActivity(intent);
                            break;

                        case "smartatips":
                            intent = new Intent(getContext(), SmartaTips.class);
                            intent.putExtra("newsCallerData", data);
                            break;

                        case "dyssa":
                            intent = new Intent(getContext(), Dyssa.class);
                            intent.putExtra("newsCallerData", data);
                            break;

                        case "verktyg":
                            intent = new Intent(getContext(), Verktyg.class);
                            intent.putExtra("newsCallerData", data);
                            break;

                        default:
                            defaultBehavior = true;
                            break;
                    }

                    if(defaultBehavior)
                    {
                        intent = new Intent(getContext(), News.class);
                        intent.putExtra("newsData", data);
                        intent.putExtra("newsTitle", title);
                        intent.putExtra("newsBody", body);
                        startActivity(intent);
                    }

                }
                return true;
            }
        });
        return rootView;
    }
}