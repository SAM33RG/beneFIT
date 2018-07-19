package tech.iosd.benefit.DashboardFragments;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import instamojo.library.InstamojoPay;
import instamojo.library.InstapayListener;
import tech.iosd.benefit.Adapters.PlansView;
import tech.iosd.benefit.R;

public class Plans extends Fragment implements ViewPager.OnPageChangeListener
{
    private int[] layouts = { R.layout.dashboard_plan_view_1, R.layout.dashboard_plan_view_2,
                              R.layout.dashboard_plan_view_3, R.layout.dashboard_plan_view_4};
    private int[] pagerIndicator = { R.id.dashboard_plan_views_pager_indicator_1,
                                    R.id.dashboard_plan_views_pager_indicator_2,
                                    R.id.dashboard_plan_views_pager_indicator_3,
                                    R.id.dashboard_plan_views_pager_indicator_4 };
    private List<ImageView> indicatorViews = new ArrayList<>();

    Context ctx;
    FragmentManager fm;
    View rootView;

    private void callInstamojoPay(String email, String phone, String amount, String purpose, String buyername) {
        final Activity activity = getActivity();
        InstamojoPay instamojoPay = new InstamojoPay();
        IntentFilter filter = new IntentFilter("ai.devsupport.instamojo");
        activity.registerReceiver(instamojoPay, filter);
        JSONObject pay = new JSONObject();
        try {
            pay.put("email", email);
            pay.put("phone", phone);
            pay.put("purpose", purpose);
            pay.put("amount", amount);
            pay.put("name", buyername);
            pay.put("send_sms", true);
            pay.put("send_email", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initListener();
        instamojoPay.start(activity, pay, listener);
    }

    InstapayListener listener;


    private void initListener() {
        listener = new InstapayListener() {
            @Override
            public void onSuccess(String response) {
                Toast.makeText(getContext(), response, Toast.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onFailure(int code, String reason) {
                Toast.makeText(getContext(), "Failed: " + reason, Toast.LENGTH_LONG)
                        .show();
            }
        };
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.dashboard_plan_views, container, false);
        ctx = rootView.getContext();
        fm = getFragmentManager();

        for (int aPagerIndicator : pagerIndicator)
            indicatorViews.add((ImageView) rootView.findViewById(aPagerIndicator));

        ViewPager mPager = rootView.findViewById(R.id.dashboard_plan_views_pager);
        mPager.setAdapter(new PlansView(layouts, ctx, fm));
        mPager.addOnPageChangeListener(this);

        Button callBtn = rootView.findViewById(R.id.nutrition_plan_call);

        if(callBtn != null)
        {
            callBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    fm.beginTransaction().replace(R.id.dashboard_content, new BodyCompositionAnalysis()).addToBackStack(null).commit();
                }
            });
        }

        Button nutrition_1 = rootView.findViewById(R.id.nutrition_plan_nut_1);
        if (nutrition_1 !=null){
            nutrition_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callInstamojoPay("sameergangar1@gmail.com","918383090979","0","nutrition 1 month","sameer");
                    Toast.makeText(getContext(),"btn",Toast.LENGTH_LONG).show();
                }
            });
        }


        return rootView;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {
        for(int i=0; i<indicatorViews.size(); i++)
            indicatorViews.get(i).setAlpha(0.3f);

        indicatorViews.get(position).setAlpha(1.0f);
    }

    @Override
    public void onPageSelected(int position) { }

    @Override
    public void onPageScrollStateChanged(int state) { }
}
