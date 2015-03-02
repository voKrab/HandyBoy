package com.vallverk.handyboy.view;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.ViewStateController.VIEW_STATE;
import com.vallverk.handyboy.model.FilterManager.SearchType;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;
import com.vallverk.handyboy.server.ServerManager;
import com.vallverk.handyboy.view.base.BaseFragment;
import com.vallverk.handyboy.view.base.FontUtils;
import com.vallverk.handyboy.view.base.FontUtils.FontStyle;
import com.vallverk.handyboy.view.feed.FeedViewFragment.CommunicationSearch;

import org.json.JSONObject;

public class BOTViewFragment extends BaseFragment
{
	private APIManager apiManager;
	private UserAPIObject user;

    private Button killSeverButton;
    private TextView consoleTextView;
    private ScrollView scrollView;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate ( R.layout.bot_layout, null );
        killSeverButton = (Button) view.findViewById(R.id.killSeverButton);
        consoleTextView = (TextView) view.findViewById(R.id.consoleTextView);
        scrollView = (ScrollView)view.findViewById(R.id.scrollView);
		return view;
	}

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );
		apiManager = APIManager.getInstance ();
		user = apiManager.getUser ();
		setListeners ();

	}


	private void setListeners ()
	{
        killSeverButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < 1000; i++){
                    new AsyncTask<Void, Void, String>() {
                        @Override
                        protected String doInBackground(Void... params) {
                            String result = "";
                            try {
                                result = ServerManager.getRequest("https://api.handyboy.com/search/?name=&sort=0&userId=639&limit=25&offset=0&latitude=49.9939862&longitude=36.2379548");
                                //Bitmap icon = BitmapFactory.decodeResource(controller.getResources(), R.drawable.);
                               // ImageLoader.getInstance().loadImage();
                                ImageLoader.getInstance().loadImage("https://api.handyboy.com/uploads/media_12_10350908012015_54aea3ada7775.png", DisplayImageOptions.createSimple(), null);
                                ImageLoader.getInstance().loadImage("https://api.handyboy.com/uploads/media_12_10350908012015_54aea3ada7775.png", DisplayImageOptions.createSimple(), null);
                                ImageLoader.getInstance().loadImage("https://api.handyboy.com/uploads/media_12_10350908012015_54aea3ada7775.png", DisplayImageOptions.createSimple(), null);
                                ImageLoader.getInstance().loadImage("https://api.handyboy.com/uploads/media_12_10350908012015_54aea3ada7775.png", DisplayImageOptions.createSimple(), null);
                                ImageLoader.getInstance().loadImage("https://api.handyboy.com/uploads/media_12_10350908012015_54aea3ada7775.png", DisplayImageOptions.createSimple(), null);
                                ImageLoader.getInstance().loadImage("https://api.handyboy.com/uploads/media_12_10350908012015_54aea3ada7775.png", DisplayImageOptions.createSimple(), null);



                                //apiManager.saveBitmap (  );
                            } catch (Exception e) {
                                result = e.getMessage();
                                e.printStackTrace();
                            }
                            return result;
                        }

                        public void onPreExecute() {
                            super.onPreExecute();
                        }

                        public void onPostExecute(String result) {
                            super.onPostExecute(result);
                            consoleTextView.setText(consoleTextView.getText() + result + "+++++++++++++++++\n");
                            scrollView.post(new Runnable() {

                                @Override
                                public void run() {
                                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                                }
                            });
                        }
                    }.execute();

                    /*try {
                        Thread.sleep(100);
                    } catch(InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }*/
                }

                for(int i = 0; i < 1000; i++){
                    new AsyncTask<Void, Void, String>() {
                        @Override
                        protected String doInBackground(Void... params) {
                            String result = "";
                            try {
                                result = ServerManager.getRequest("https://api.handyboy.com/search/?name=&sort=0&userId=639&limit=25&offset=0&latitude=49.9939862&longitude=36.2379548");
                                //Bitmap icon = BitmapFactory.decodeResource(controller.getResources(), R.drawable.);
                                // ImageLoader.getInstance().loadImage();
                                ImageLoader.getInstance().loadImage("https://api.handyboy.com/uploads/media_12_10350908012015_54aea3ada7775.png", DisplayImageOptions.createSimple(), null);
                                ImageLoader.getInstance().loadImage("https://api.handyboy.com/uploads/media_12_10350908012015_54aea3ada7775.png", DisplayImageOptions.createSimple(), null);
                                ImageLoader.getInstance().loadImage("https://api.handyboy.com/uploads/media_12_10350908012015_54aea3ada7775.png", DisplayImageOptions.createSimple(), null);
                                ImageLoader.getInstance().loadImage("https://api.handyboy.com/uploads/media_12_10350908012015_54aea3ada7775.png", DisplayImageOptions.createSimple(), null);
                                ImageLoader.getInstance().loadImage("https://api.handyboy.com/uploads/media_12_10350908012015_54aea3ada7775.png", DisplayImageOptions.createSimple(), null);
                                ImageLoader.getInstance().loadImage("https://api.handyboy.com/uploads/media_12_10350908012015_54aea3ada7775.png", DisplayImageOptions.createSimple(), null);



                                //apiManager.saveBitmap (  );
                            } catch (Exception e) {
                                result = e.getMessage();
                                e.printStackTrace();
                            }
                            return result;
                        }

                        public void onPreExecute() {
                            super.onPreExecute();
                        }

                        public void onPostExecute(String result) {
                            super.onPostExecute(result);
                            consoleTextView.setText(consoleTextView.getText() + result + "+++++++++++++++++\n");
                            scrollView.post(new Runnable() {

                                @Override
                                public void run() {
                                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                                }
                            });
                        }
                    }.execute();

                    /*try {
                        Thread.sleep(100);
                    } catch(InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }*/
                }

                for(int i = 0; i < 1000; i++){
                    new AsyncTask<Void, Void, String>() {
                        @Override
                        protected String doInBackground(Void... params) {
                            String result = "";
                            try {
                                result = ServerManager.getRequest("https://api.handyboy.com/search/?name=&sort=0&userId=639&limit=25&offset=0&latitude=49.9939862&longitude=36.2379548");
                                //Bitmap icon = BitmapFactory.decodeResource(controller.getResources(), R.drawable.);
                                // ImageLoader.getInstance().loadImage();

                                //ImageLoader.getInstance().loadImage("https://api.handyboy.com/uploads/media_12_10350908012015_54aea3ada7775.png", DisplayImageOptions.createSimple(), null);
                                //ImageLoader.getInstance().loadImage("https://api.handyboy.com/uploads/media_12_10350908012015_54aea3ada7775.png", DisplayImageOptions.createSimple(), null);
                                //ImageLoader.getInstance().loadImage("https://api.handyboy.com/uploads/media_12_10350908012015_54aea3ada7775.png", DisplayImageOptions.createSimple(), null);
                                //ImageLoader.getInstance().loadImage("https://api.handyboy.com/uploads/media_12_10350908012015_54aea3ada7775.png", DisplayImageOptions.createSimple(), null);
                                ImageLoader.getInstance().loadImage("https://api.handyboy.com/uploads/media_12_10350908012015_54aea3ada7775.png", DisplayImageOptions.createSimple(), new ImageLoadingListener() {
                                    @Override
                                    public void onLoadingStarted(String s, View view) {

                                    }

                                    @Override
                                    public void onLoadingFailed(String s, View view, FailReason failReason) {

                                    }

                                    @Override
                                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                                        consoleTextView.setText("++++++download complete");
                                    }

                                    @Override
                                    public void onLoadingCancelled(String s, View view) {

                                    }
                                });



                                //apiManager.saveBitmap (  );
                            } catch (Exception e) {
                                result = e.getMessage();
                                e.printStackTrace();
                            }
                            return result;
                        }

                        public void onPreExecute() {
                            super.onPreExecute();
                        }

                        public void onPostExecute(String result) {
                            super.onPostExecute(result);
                            consoleTextView.setText(consoleTextView.getText() + result + "+++++++++++++++++\n");
                            scrollView.post(new Runnable() {

                                @Override
                                public void run() {
                                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                                }
                            });
                        }
                    }.execute();

                    /*try {
                        Thread.sleep(100);
                    } catch(InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }*/
                }
            }
        });
	}
}
