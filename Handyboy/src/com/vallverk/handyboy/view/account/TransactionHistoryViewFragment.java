package com.vallverk.handyboy.view.account;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.Tools;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.AdditionalChargesAPIObject;
import com.vallverk.handyboy.model.api.BookingDataManager;
import com.vallverk.handyboy.model.api.BookingDataObject;
import com.vallverk.handyboy.model.api.MyMoneyAPIObject;
import com.vallverk.handyboy.model.api.AddonServiceAPIObject.AddonServiceAPIParams;
import com.vallverk.handyboy.model.api.AddressAPIObject.AddressParams;
import com.vallverk.handyboy.model.api.BookingAPIObject.BookingAPIParams;
import com.vallverk.handyboy.model.api.BookingDataObject.JobAddonDetailsObject;
import com.vallverk.handyboy.model.api.JobAddonsAPIObject.JobAddonsAPIParams;
import com.vallverk.handyboy.model.api.MyMoneyAPIObject.MyMoneyParams;
import com.vallverk.handyboy.model.api.TypeJobServiceAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;
import com.vallverk.handyboy.server.ServerManager;
import com.vallverk.handyboy.view.base.AnimatedExpandableListView;
import com.vallverk.handyboy.view.base.BaseFragment;
import com.vallverk.handyboy.view.base.AnimatedExpandableListView.AnimatedExpandableListAdapter;

public class TransactionHistoryViewFragment extends BaseFragment
{
    private ImageView backImageView;
    private TextView backTextView;

    private AnimatedExpandableListView transactionExpandableListView;
    private TransactionHistoryAdapter transactionHistoryAdapter;

    private List < GroupItem > groupItems;

    public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        View view = inflater.inflate ( R.layout.transaction_history_layout, null );
        backImageView = ( ImageView ) view.findViewById ( R.id.backImageView );
        backTextView = ( TextView ) view.findViewById ( R.id.backTextView );
        transactionExpandableListView = ( AnimatedExpandableListView ) view.findViewById ( R.id.transactionExpandableListView );
        return view;
    }

    @Override
    protected void init ()
    {
        new AsyncTask < Void, Void, String > ()
        {

            @Override
            protected String doInBackground ( Void... params )
            {
                String status = "";
                try
                {
                    String request = ServerManager.getRequest ( ServerManager.GET_TRANSACTION_HISTORY + APIManager.getInstance().getUser().getId() );
                    JSONObject jsonRequest = new JSONObject ( request );
                    status = jsonRequest.getString ( "parameters" );
                    JSONArray list = jsonRequest.getJSONArray ( "list" );
                    groupItems = new ArrayList < GroupItem >();
                    for ( int i = 0; i < list.length(); i++ )
                    {
                        try{
                            GroupItem groupItem = new GroupItem();
                            groupItem.moneyAPIObject = new MyMoneyAPIObject ( list.getJSONObject(i) );
                            JSONObject booking = list.getJSONObject ( i ).getJSONObject("booking");
                            groupItem.bookingDataObject =  new BookingDataObject (booking);
                            groupItem.additionalChargesList = new ArrayList<AdditionalChargesAPIObject>();
                            JSONArray chargesList = booking.getJSONArray("addcharges"); // new JSONArray(booking.getJSONObject("addcharges"));
                            for (int j = 0; j < chargesList.length(); j++){
                                groupItem.additionalChargesList.add(new AdditionalChargesAPIObject(chargesList.getJSONObject(j)));
                            }
                            groupItems.add ( groupItem );
                        }catch (Exception ex){
                            ex.printStackTrace ();
                        }

                    }
                } catch ( Exception e )
                {
                    e.printStackTrace ();
                }
                return status;
            }

            public void onPreExecute ()
            {
                super.onPreExecute ();
                controller.showLoader ();
            }

            public void onPostExecute ( String status )
            {
                super.onPostExecute ( status );
                if(status.isEmpty ()){
                    controller.hideLoader ();
                    transactionHistoryAdapter.setData ( groupItems );
                    transactionExpandableListView.setAdapter ( transactionHistoryAdapter );
                }
            }
        }.execute ();
    }

    @Override
    public void onActivityCreated ( Bundle savedInstanceState )
    {
        super.onActivityCreated ( savedInstanceState );
        updateComponents ();
        addListeners ();
        updateFonts ();
    }

    @SuppressLint("NewApi")
    private void updateComponents ()
    {
        transactionHistoryAdapter = new TransactionHistoryAdapter ( controller );

        if ( android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2 )
        {
            transactionExpandableListView.setIndicatorBounds ( MainActivity.SCREEN_WIDTH - Tools.fromDPToPX ( 30, controller ), MainActivity.SCREEN_WIDTH );

        } else
        {
            transactionExpandableListView.setIndicatorBoundsRelative ( MainActivity.SCREEN_WIDTH - Tools.fromDPToPX ( 30, controller ), MainActivity.SCREEN_WIDTH );
        }


    }

    protected void addListeners ()
    {
        backTextView.setOnClickListener ( new OnClickListener ()
        {

            @Override
            public void onClick ( View v )
            {
                controller.onBackPressed ();
            }
        } );

        backImageView.setOnClickListener ( new OnClickListener ()
        {

            @Override
            public void onClick ( View view )
            {
                controller.onBackPressed ();
            }
        } );


        transactionExpandableListView.setOnGroupClickListener ( new OnGroupClickListener ()
        {

            @Override
            public boolean onGroupClick ( ExpandableListView parent, View v, int groupPosition, long id )
            {
                if ( transactionExpandableListView.isGroupExpanded ( groupPosition ) )
                {
                    transactionExpandableListView.collapseGroupWithAnimation ( groupPosition );
                } else
                {
                    transactionExpandableListView.expandGroupWithAnimation ( groupPosition );
                }

                return true;
            }

        } );
    }

    @Override
    protected void updateFonts ()
    {
    }

    private static class GroupItem
    {
        MyMoneyAPIObject moneyAPIObject;
        BookingDataObject bookingDataObject;
        List<AdditionalChargesAPIObject> additionalChargesList;
    }

    private static class ChildHolder
    {
        TextView addressNameTextView;
        TextView addressTextView;
        TextView periodTextView;
        TextView dateTextView;
        TextView priceTextView;
        LinearLayout addonsContainerLayout;
    }

    private static class GroupHolder
    {
        TextView gigIdTextView;
        TextView gigNameTextView;
        TextView gigSessionNameTextView;
        TextView gigPriceTextView;
    }

    private class TransactionHistoryAdapter extends AnimatedExpandableListAdapter
    {
        private LayoutInflater inflater;
        private List < GroupItem > items;

        public TransactionHistoryAdapter ( Context context )
        {
            inflater = LayoutInflater.from ( context );
        }

        public void setData ( List < GroupItem > items )
        {
            this.items = items;
            notifyDataSetChanged ();
        }

        @Override
        public GroupItem getChild ( int groupPosition, int childPosition )
        {
            return items.get ( groupPosition );
        }

        @Override
        public long getChildId ( int groupPosition, int childPosition )
        {
            return childPosition;
        }

        @Override
        public View getRealChildView ( int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent )
        {
			/*
			 * TextView addressNameTextView;
		TextView addressTextView;
		TextView periodTextView;
		TextView dateTextView;
		TextView priceTextView;
		LinearLayout addonsContainerLayout;*/
            ChildHolder holder;
            BookingDataObject bookingDataObject = getChild ( groupPosition, childPosition ).bookingDataObject;
            List<AdditionalChargesAPIObject> additionalChargesList = getChild ( groupPosition, childPosition ).additionalChargesList;

            if ( convertView == null )
            {
                holder = new ChildHolder ();
                convertView = inflater.inflate ( R.layout.transaction_child_item, parent, false );
                holder.addressNameTextView = (TextView) convertView.findViewById ( R.id.addressNameTextView );
                holder.addressTextView = (TextView) convertView.findViewById ( R.id.addressTextView );
                holder.periodTextView = (TextView) convertView.findViewById ( R.id.periodTextView );
                holder.dateTextView = (TextView) convertView.findViewById ( R.id.dateTextView );
                holder.priceTextView = (TextView) convertView.findViewById ( R.id.priceTextView );

                holder.addonsContainerLayout = (LinearLayout) convertView.findViewById ( R.id.addonsContainerLayout );
                convertView.setTag ( holder );
            } else
            {
                holder = ( ChildHolder ) convertView.getTag ();
            }

            holder.addressNameTextView.setText ( bookingDataObject.getAddress ().getString ( AddressParams.DESCRIPTION ) );
            holder.addressTextView.setText ( bookingDataObject.getAddress ().getString ( AddressParams.ADDRESS ) );
            holder.dateTextView.setText ( Tools.toDateString ( bookingDataObject.getBookingAPIObject ().getString ( BookingAPIParams.DATE ) ) );

            JSONArray timeJsonArray;
            JSONObject timePeriod;
            String startPeriod = "";
            String endPeriod = "";
            try
            {
                timeJsonArray = new JSONArray ( bookingDataObject.getBookingAPIObject ().getValue ( BookingAPIParams.TIME ).toString () );
                timePeriod = timeJsonArray.getJSONObject ( 0 );
                startPeriod = timePeriod.getString ( "start" );
                endPeriod = timePeriod.getString ( "end" );
            } catch ( JSONException e )
            {
                e.printStackTrace ();
            }
            float hours = Float.parseFloat(bookingDataObject.getBookingAPIObject ().getValue ( BookingAPIParams.TOTAL_HOURS ).toString ());
            String sHours = " Hour";
            if(hours > 1){
                sHours = " Hours";
            }
            holder.periodTextView.setText ( Tools.decimalHoursFormat(hours) + sHours + ", " + Tools.getAmericanTime(startPeriod) + " to " + Tools.getAmericanTime(endPeriod));
            //holder.priceTextView.setText ( "$" + bookingDataObject.getBookingAPIObject ().getValue ( BookingAPIParams.TOTAL_PRICE ).toString () );
            float priceByHour = Float.parseFloat(bookingDataObject.getBookingAPIObject ().getValue ( BookingAPIParams.TOTAL_HOURS ).toString ()) * Float.parseFloat(bookingDataObject.getTypeJobAPIObject().getValue(TypeJobServiceAPIObject.TypeJobServiceParams.PRICE).toString());
            holder.priceTextView.setText ( "$" +  Tools.decimalFormat(priceByHour));
            holder.addonsContainerLayout.removeAllViews ();
            for ( JobAddonDetailsObject jobAddonDetailsObject : bookingDataObject.getAddonsAPIObjects () )
            {
                View addonItemView = inflater.inflate ( R.layout.addon_item_view, null );
                TextView addonNameTextView = ( TextView ) addonItemView.findViewById ( R.id.addonNameTextView );
                TextView addonPriceTextView = ( TextView ) addonItemView.findViewById ( R.id.addonPriceTextView );
                addonNameTextView.setText ( "+" + jobAddonDetailsObject.addonsAPIObject.getValue ( JobAddonsAPIParams.NAME ) );
                float price = Float.parseFloat(jobAddonDetailsObject.addonServiceAPIObject.getValue ( AddonServiceAPIParams.PRICE ).toString());
                addonPriceTextView.setText ( "$" + Tools.decimalFormat(price) );
                holder.addonsContainerLayout.addView ( addonItemView );
            }

            for ( AdditionalChargesAPIObject additionalChargesAPIObject : additionalChargesList )
            {
                View addonItemView = inflater.inflate ( R.layout.addon_item_view, null );
                TextView addonNameTextView = ( TextView ) addonItemView.findViewById ( R.id.addonNameTextView );
                TextView addonPriceTextView = ( TextView ) addonItemView.findViewById ( R.id.addonPriceTextView );
                addonNameTextView.setText ( "+" + additionalChargesAPIObject.getValue ( AdditionalChargesAPIObject.AdditionalChargesParams.REASON ).toString() );
                float price = Float.parseFloat(additionalChargesAPIObject.getValue (AdditionalChargesAPIObject.AdditionalChargesParams.PRICE).toString());
                addonPriceTextView.setText ( "$" + Tools.decimalFormat(price) );
                holder.addonsContainerLayout.addView ( addonItemView );
            }

            View addonItemView = inflater.inflate ( R.layout.addon_item_view, null );
            TextView addonNameTextView = ( TextView ) addonItemView.findViewById ( R.id.addonNameTextView );
            TextView addonPriceTextView = ( TextView ) addonItemView.findViewById ( R.id.addonPriceTextView );
            addonNameTextView.setText ( "+tip");
            float price = Float.parseFloat(bookingDataObject.getBookingAPIObject().getValue (BookingAPIParams.TIP).toString());
            addonPriceTextView.setText ( "$" + Tools.decimalFormat(price) );
            holder.addonsContainerLayout.addView ( addonItemView );

            return convertView;
        }

        @Override
        public int getRealChildrenCount ( int groupPosition )
        {
            return 1;
        }

        @Override
        public MyMoneyAPIObject getGroup ( int groupPosition )
        {
            return items.get ( groupPosition ).moneyAPIObject;
        }

        @Override
        public int getGroupCount ()
        {
            return items.size ();
        }

        @Override
        public long getGroupId ( int groupPosition )
        {
            return groupPosition;
        }

        @Override
        public View getGroupView ( int groupPosition, boolean isExpanded, View convertView, ViewGroup parent )
        {
            GroupHolder holder;
            MyMoneyAPIObject myMoneyAPIObject = getGroup ( groupPosition );
            BookingDataObject bookingDataObject = getChild ( groupPosition, 0 ).bookingDataObject;
            if ( convertView == null )
            {
                holder = new GroupHolder ();
                convertView = inflater.inflate ( R.layout.transaction_group_item, parent, false );
                holder.gigIdTextView = ( TextView ) convertView.findViewById ( R.id.gigIdTextView );
                holder.gigNameTextView = ( TextView ) convertView.findViewById ( R.id.gigNameTextView );
                holder.gigSessionNameTextView = ( TextView ) convertView.findViewById ( R.id.gigSessionNameTextView );
                holder.gigPriceTextView = ( TextView ) convertView.findViewById ( R.id.gigPriceTextView );
                convertView.setTag ( holder );
            } else
            {
                holder = ( GroupHolder ) convertView.getTag ();
            }

            holder.gigIdTextView.setText ( "GIG#" + bookingDataObject.getBookingAPIObject().getValue("bookingId") );
            holder.gigNameTextView.setText ( bookingDataObject.getService ().getShortName () );
            holder.gigSessionNameTextView.setText ( bookingDataObject.getTypeJobAPIObject ().getName () + " Session" );

            float price = Float.parseFloat(myMoneyAPIObject.getString ( MyMoneyParams.AMOUNT ).toString());
            holder.gigPriceTextView.setText ( "$" + Tools.decimalFormat(price) );
            return convertView;
        }

        @Override
        public boolean hasStableIds ()
        {
            return true;
        }

        @Override
        public boolean isChildSelectable ( int arg0, int arg1 )
        {
            return true;
        }

    }

}