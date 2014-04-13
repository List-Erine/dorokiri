package net.listerine.sample.dorokiri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

		//テキストファイルの読み込み
		try {
			AssetManager assets = getResources().getAssets();
			InputStream in = assets.open("scene1.ks");
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line;
			String tmp;
			MediaPlayer mp = new MediaPlayer();
			while((line = br.readLine()) != null){

				//空白行は無視
				if(line.length()<1){
					continue;
				}


				//コメント行は無視
				tmp = line.substring(0,1);
				if(tmp.equals(";")){
					continue;
				}

				//大かっこから始まる命令行は解析する
				tmp = line.substring(0,1);
				if(tmp.equals("[")){

					//大かっこ内の取得
					String regex = "\\[(.+?)\\]";
					String target = line;
					String result = null;
					Pattern pattern = Pattern.compile(regex);
					Matcher matcher = pattern.matcher(target);
					if(matcher.find()){
						result = matcher.group(1);
					}

					//スペースで区切る
					String[] params = result.split(" ");

					//画像セットの時
					if(params[0].equals("image")){
						String storage = null;
						String layer = null;
						String page = null;

						//パラメータの取得
						for(int i=1;i<params.length;i++){
							String[] param = params[i].split("=");

							if(param[0].equals("storage")){

								String regex2 = "\\\"(.+?)\\\"";
								String target2 = param[1];
								Pattern pattern2 = Pattern.compile(regex2);
								Matcher matcher2 = pattern2.matcher(target2);
								if(matcher2.find()){
									storage = matcher2.group(1);
								}


							}

							if(param[0].equals("layer")){
								layer = param[1];
							}

							if(param[0].equals("page")){
								page = param[1];
							}
						}


						//画像をセットする
						if(layer.equals("base")){

							ImageView v = (ImageView)findViewById(R.id.imageBase);
							InputStream is = getResources().getAssets().open(storage);
							Bitmap bm = BitmapFactory.decodeStream(is);
							v.setImageBitmap(bm);


						}

					}
					//bgmセットの時
					else if(params[0].equals("fadeinbgm")){
						String storage = null;
						String time = null;
						String loop = null;

						//パラメータの取得
						for(int i=1;i<params.length;i++){
							String[] param = params[i].split("=");

							if(param[0].equals("storage")){
								storage = param[1];
							}

							if(param[0].equals("time")){
								time = param[1];
							}

							if(param[0].equals("loop")){
								loop = param[1];
							}
						}

						//再生
						AssetFileDescriptor afd = getAssets().openFd(storage);
						mp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
						mp.setDisplay(null);
						mp.prepare();

					}





				}
				//大かっこ開始でない行は
				else{

				}

			}

		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}








	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
