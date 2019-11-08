package com.hmi.dealsnxt.Adaptor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.location.Location;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hmi.dealsnxt.Activity.DetailNewActivity;
import com.hmi.dealsnxt.Fragement.HotDealsFragment;
import com.hmi.dealsnxt.HelperClass.Constaints;
import com.hmi.dealsnxt.HelperClass.Global;
import com.hmi.dealsnxt.HelperClass.SessionManager;
import com.hmi.dealsnxt.Model.HotDealsModel;
import com.hmi.dealsnxt.R;
import com.hmi.dealsnxt.Utils.Customutils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AllinoneAdaptor extends RecyclerView.Adapter<AllinoneAdaptor.SimpleItemViewHolder> {
    private List<HotDealsModel> items;
    public Activity activity;
    public int count = 0;
    public Context context;
    String category_id;
    public static String Dealid;
    com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    public static String OutlletName, OutlletAddress, OutletInTime, OutletOutTime, Dealdescription;
    DisplayImageOptions options;
    // public String Dealid="";


    // Provide a reference to the views for each data item
// Provide access to all the views for a data item in a view holder
    public final static class SimpleItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvdiscount, tvlikecount, tvdealname, tvlocation, tvstartime, tvactualprice, tvendtime;
        TextView tvwaiveoffrs, tvoptiontwo, tvoptionone, tvoptionthree, date;
        LinearLayout LLoffer;
        View viewline;
        ImageView dealimg, ivlike;
        RelativeLayout RLpercent, LLbanner;
        LinearLayout LLoption;
        LinearLayout LLView;
        public TextView tvcount,distance,tvDealsCount;

        //  public TextView tvcount;

        public SimpleItemViewHolder(View itemView) {
            super(itemView);
            //   LLbanner = (RelativeLayout) itemView.findViewById(R.id.LLbanner);

            LLView = (LinearLayout) itemView.findViewById(R.id.LLView);
            RLpercent = (RelativeLayout) itemView.findViewById(R.id.RLpercent);
            tvdiscount = (TextView) itemView.findViewById(R.id.tvdiscount);
            tvlikecount = (TextView) itemView.findViewById(R.id.tvlikecount);
            tvdealname = (TextView) itemView.findViewById(R.id.tvdealname);
            tvlocation = (TextView) itemView.findViewById(R.id.tvlocation);
            tvstartime = (TextView) itemView.findViewById(R.id.tvstartime);
            tvendtime = (TextView) itemView.findViewById(R.id.tvendtime);
            tvactualprice = (TextView) itemView.findViewById(R.id.tvactualprice);
            viewline = (View) itemView.findViewById(R.id.viewline);
            tvwaiveoffrs = (TextView) itemView.findViewById(R.id.tvwaiveoffrs);
            LLoption = (LinearLayout) itemView.findViewById(R.id.LLoption);
            tvoptionone = (TextView) itemView.findViewById(R.id.tvoptionone);
        /*    tvoptiontwo = (TextView) itemView.findViewById(R.id.tvoptiontwo);
            tvoptionthree = (TextView) itemView.findViewById(R.id.tvoptionthree);*/
            tvcount = (TextView) itemView.findViewById(R.id.tvcount);
            LLoffer = (LinearLayout) itemView.findViewById(R.id.LLoffer);
            date = (TextView) itemView.findViewById(R.id.date);
            distance = (TextView) itemView.findViewById(R.id.distance);
            dealimg = (ImageView) itemView.findViewById(R.id.dealimg);
            ivlike = (ImageView) itemView.findViewById(R.id.ivlike);
            tvDealsCount=(TextView)itemView.findViewById(R.id.tvDealsCount);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AllinoneAdaptor(List<HotDealsModel> items, Activity _activity, Context context,String category_id) {
        this.items = items;
        this.activity = _activity;
        this.context = context;
        this.category_id=category_id;

        options = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .showImageOnLoading(R.drawable.banner).showImageForEmptyUri(R.drawable.banner).showImageOnFail(R.drawable.banner)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(activity));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.items.size();
    }

    // Create new items (invoked by the layout manager)
    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public SimpleItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View itemView;
        if (viewType == 0) {
            itemView = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.row_hotdeals_new, viewGroup, false);
        } else {
            itemView = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.row_hotdeals_new, viewGroup, false);
        }
        return new SimpleItemViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    // Involves populating data into the item through holder
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final SimpleItemViewHolder viewHolder, final int position) {


        viewHolder.tvdealname.setText(items.get(position).getOutletName());
        viewHolder.tvstartime.setText(Customutils.dateFormat(items.get(position).getAvailibiltyTime()));
        viewHolder.tvendtime.setText(Customutils.dateFormat(items.get(position).getReminderTime()));
        viewHolder.tvactualprice.setText("\u20B9" + items.get(position).getActualPrice());
        viewHolder.tvwaiveoffrs.setText("\u20B9"+ items.get(position).getAfterDiscountPrice());

        if (!items.get(position).getDealCount().equals(""))
        if(Integer.parseInt(items.get(position).getDealCount())>1){
            viewHolder.tvDealsCount.setVisibility(View.VISIBLE);
            viewHolder.tvDealsCount.setText("+" + (Integer.parseInt(items.get(position).getDealCount())-1 + " More"));
        }else {
            viewHolder.tvDealsCount.setVisibility(View.GONE);
        }

        System.out.println("deal count "+ items.get(position).getDealCount());
        Location loc1 = new Location("");
        try {
            loc1.setLatitude(Double.valueOf(SessionManager.getLatitude(context)));
            loc1.setLongitude(Double.valueOf(SessionManager.getLongitude(context)));
            Location loc2 = new Location("");
            loc2.setLatitude(Double.valueOf(items.get(position).getOutletLatitude()));
            loc2.setLongitude(Double.valueOf(items.get(position).getOutletLongtitude()));
            float distanceInMeters = loc1.distanceTo(loc2);
            viewHolder.distance.setText((new DecimalFormat("##.##").format(distanceInMeters * 0.001)) + " km");

        } catch (Exception e) {

        }
        //to make the text strike through
        //textview.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);


        //viewHolder.tvcount.setText("" + items.get(position).getNumofOffers());

        System.out.println("count "+items.get(position).getNumofOffers());
        viewHolder.tvcount.setText( items.get(position).getNumofOffers()+" Left");

        /*if (items.get(position).getNumofOffers() == 1) {
            viewHolder.LLoffer.setVisibility(View.GONE);
        } else {
            viewHolder.tvcount.setText("" + (items.get(position).getNumofOffers() - 1));

        }*/

        if (items.get(position).getNumofOffers().equals("0")){
            viewHolder.tvcount.setText("Sold Out");
        }
        if (items.get(position).getShowPercentage().equals("1") || items.get(position).getShowPercentage()=="1") {
            viewHolder.tvoptionone.setText(items.get(position).getPercentage()+" Off on " +items.get(position).getDealTitle());

        }else{
            viewHolder.tvoptionone.setText(items.get(position).getDealTitle());
        }
        System.out.println("data "+items.get(position).getDealTitle());
        viewHolder.tvlocation.setText(items.get(position).getOutletAddress());
        viewHolder.tvdiscount.setText(items.get(position).getPercentage());

            viewHolder.tvlikecount.setText(items.get(position).getLikesCount());
        //     viewHolder.ivlike.setText(items.get(position).getLikes());
    /*    if ((items.get(position).getLikes()).equals(1)) {
            viewHolder.ivlike.setImageResource(R.drawable.like);
            viewHolder.ivlike.setClickable(false);
        }*/

        OutlletName = items.get(position).getOutletName();
        OutlletAddress = (items.get(position).getOutletAddress() + "," + items.get(position).getOutletCity());
        OutletInTime = (items.get(position).getAvailibiltyTime());
        OutletOutTime = (items.get(position).getReminderTime());
        viewHolder.tvactualprice.setPaintFlags(viewHolder.tvactualprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        imageLoader.displayImage(items.get(position).getDealimage(), viewHolder.dealimg, options);

        viewHolder.LLView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dealid = String.valueOf(items.get(position).getDealid());
                //  increasecount(viewHolder.tvlikecount);
                HotDealsModel.setHotDealsModel(items.get(position));
                viewHolder.ivlike.setImageResource(R.drawable.like);
                Intent i = new Intent(activity, DetailNewActivity.class);
                i.putExtra("outletid", items.get(position).getOutletid());
                i.putExtra("quantity", items.get(position).getNumofOffers());
                i.putExtra("category_id",category_id);
                System.out.println("numOfoffers "+ items.get(position).getDealCount());
                i.putExtra("numberOfOffes",items.get(position).getDealCount());
                activity.startActivity(i);
            }
        });

       /* viewHolder.LLView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(items.get(position).getLikes()) == 0) {
                    //  imageLoader.displayImage("drawable://" + R.drawable.like, viewHolder.ivlike, options);
                    viewHolder.ivlike.setImageResource(R.drawable.like);
                    items.get(position).setLikes(String.valueOf(1));
                } else {
                    viewHolder.ivlike.setImageResource(R.drawable.dislike);
                    // imageLoader.displayImage("drawable://" + R.drawable.dislike, viewHolder.ivlike, options);
                }

                Dealid = String.valueOf(items.get(position).getDealid());
                increasecount(viewHolder.tvlikecount);
            }
        });*/

    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0;
        if (items.get(position).getType() == 0) {
            viewType = 0;
        } else {
            viewType = 1;
        }
        return viewType;
    }

    public Bitmap getBitmapFromURL(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void increasecount(final TextView textView) {
        if (Global.isInternetAvail(activity)) {
            String url = Constaints.DealLikeCount;
            HotDealsFragment.progressBar.setVisibility(View.VISIBLE);
            StringRequest request = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jSONObject = new JSONObject(new String(response));
                                String Status = jSONObject.optString("status");
                                if (Integer.parseInt(Status) == 1) {
                                    textView.setText(jSONObject.getString("likes"));
                                } else {
                                    Toast.makeText(context, jSONObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Log.e("", e.getMessage());
                            }
                            HotDealsFragment.progressBar.setVisibility(View.GONE);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    HotDealsFragment.progressBar.setVisibility(View.GONE);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("XAPIKEY", "XXXXX");
                    params.put("deal_id", Dealid);
                    params.put("category_id",category_id);
                    params.put("user_id", SessionManager.getUserID(context));
                    System.out.println("data "+params);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    return params;
                }
            };
            int socketTimeout = 30000;
            Volley.newRequestQueue(context).add(request);
            request.setRetryPolicy(new DefaultRetryPolicy(
                    socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } else {
            Toast.makeText(activity, R.string.ConnectionErrorResponse, Toast.LENGTH_LONG).show();
        }
    }
}

