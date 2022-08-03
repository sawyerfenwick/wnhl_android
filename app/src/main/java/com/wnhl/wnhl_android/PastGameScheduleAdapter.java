package com.wnhl.wnhl_android;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class PastGameScheduleAdapter extends RecyclerView.Adapter<PastGameScheduleAdapter.ViewHolder> {

    List<String> title, date;
    List<Integer> venue, eventIDs, hscore, ascore;
    Context context;
    DataBaseHelper dataBaseHelper;
    SharedPrefHelper sharedPrefHelper;
    DateFormat dateFormat;
    Date dateObj;
    String d = "";

    /**
     *
     * @param context
     * @param eventIDs
     * @param title
     * @param date
     * @param venue
     */
    public PastGameScheduleAdapter(Context context, List<Integer> eventIDs, List<String> title,
                                   List<String> date, List<Integer> venue){
        this.context = context;
        this.eventIDs = eventIDs;
        this.title = title;
        this.date = date;
        this.venue = venue;
    }//constructor

    /**
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public PastGameScheduleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.schedule_cardview, parent, false);
        return new ViewHolder(v);
    }//onCreateViewHolder

    /**
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull PastGameScheduleAdapter.ViewHolder holder, int position) {
        dataBaseHelper = new DataBaseHelper(context);
        sharedPrefHelper = new SharedPrefHelper();

        String headlineSplit[] = title.get(position).split("vs");
        String uri;
        int imageID;
        int home = dataBaseHelper.getHomeTeam(eventIDs.get(position));
        int away = dataBaseHelper.getAwayTeam(eventIDs.get(position));
        int homeScore = dataBaseHelper.getHomeScore(eventIDs.get(position));
        int awayScore = dataBaseHelper.getAwayScore(eventIDs.get(position));

        switch (home){
            case 940:
                //Steelers
                uri = "@drawable/steelers_nobackground";
                imageID = context.getResources().getIdentifier(uri, null,
                        context.getPackageName());
                holder.home.setImageDrawable(context.getResources().getDrawable(imageID,null));
                break;
            case 1370:
                //Townline
                uri = "@drawable/townline_nobackground";
                imageID = context.getResources().getIdentifier(uri, null,
                        context.getPackageName());
                holder.home.setImageDrawable(context.getResources().getDrawable(imageID,null));
                break;
            case 1371:
                //CrownRoom
                uri = "@drawable/crownroom_nobackground";
                imageID = context.getResources().getIdentifier(uri, null,
                        context.getPackageName());
                holder.home.setImageDrawable(context.getResources().getDrawable(imageID,null));
                break;
            case 1810:
                //DainCity
                uri = "@drawable/dusters_nobackground";
                imageID = context.getResources().getIdentifier(uri, null,
                        context.getPackageName());
                holder.home.setImageDrawable(context.getResources().getDrawable(imageID,null));
                break;
            case 1822:
                //Lincoln
                uri = "@drawable/legends_nobackground";
                imageID = context.getResources().getIdentifier(uri, null,
                        context.getPackageName());
                holder.home.setImageDrawable(context.getResources().getDrawable(imageID,null));
                break;
            case 1824:
                //Merrit
                uri = "@drawable/islanders";
                imageID = context.getResources().getIdentifier(uri, null,
                        context.getPackageName());
                holder.home.setImageDrawable(context.getResources().getDrawable(imageID,null));
                break;
            default:
                uri = "@drawable/wnhl_logo";
                imageID = context.getResources().getIdentifier(uri, null,
                        context.getPackageName());
                holder.home.setImageDrawable(context.getResources().getDrawable(imageID,null));
        }

        switch (away){
            case 940:
                //Steelers
                uri = "@drawable/steelers_nobackground";
                imageID = context.getResources().getIdentifier(uri, null,
                        context.getPackageName());
                holder.away.setImageDrawable(context.getResources().getDrawable(imageID,null));
                break;
            case 1370:
                //Townline
                uri = "@drawable/townline_nobackground";
                imageID = context.getResources().getIdentifier(uri, null,
                        context.getPackageName());
                holder.away.setImageDrawable(context.getResources().getDrawable(imageID,null));
                break;
            case 1371:
                //CrownRoom
                uri = "@drawable/crownroom_nobackground";
                imageID = context.getResources().getIdentifier(uri, null,
                        context.getPackageName());
                holder.away.setImageDrawable(context.getResources().getDrawable(imageID,null));
                break;
            case 1810:
                //DainCity
                uri = "@drawable/dusters_nobackground";
                imageID = context.getResources().getIdentifier(uri, null,
                        context.getPackageName());
                holder.away.setImageDrawable(context.getResources().getDrawable(imageID,null));
                break;
            case 1822:
                //Lincoln
                uri = "@drawable/legends_nobackground";
                imageID = context.getResources().getIdentifier(uri, null,
                        context.getPackageName());
                holder.away.setImageDrawable(context.getResources().getDrawable(imageID,null));
                break;
            case 1824:
                //Merrit
                uri = "@drawable/islanders";
                imageID = context.getResources().getIdentifier(uri, null,
                        context.getPackageName());
                holder.away.setImageDrawable(context.getResources().getDrawable(imageID,null));
                break;
            default:
                uri = "@drawable/wnhl_logo";
                imageID = context.getResources().getIdentifier(uri, null,
                        context.getPackageName());
                holder.away.setImageDrawable(context.getResources().getDrawable(imageID,null));
        }

        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            dateObj = dateFormat.parse(date.get(position));
            dateFormat = new SimpleDateFormat("EEE MMM d, yyyy");
            d = dateFormat.format(dateObj);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.day.setText(d);
        if(homeScore == -1 | awayScore == -1){
            holder.scoretime.setText("Score Not Available");
        }
        else{
            holder.scoretime.setText(homeScore + " - " + awayScore);
        }
        holder.scoretime.setTypeface(Typeface.DEFAULT_BOLD);
        holder.location.setText(dataBaseHelper.getVenueName(venue.get(position)));
        holder.headline.setText(title.get(position));
    }//onBindViewHolder

    /**
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return title.size();
    }//getItemCount

    /**
     *
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView day, scoretime, location, headline;
        ImageView home, away;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.date);
            scoretime = itemView.findViewById(R.id.scoretime);
            location = itemView.findViewById(R.id.locate);
            headline = itemView.findViewById(R.id.gameHeadline);
            home = itemView.findViewById(R.id.home);
            away = itemView.findViewById(R.id.away);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }//ViewHolder
}//PastGameScheduleAdapter
