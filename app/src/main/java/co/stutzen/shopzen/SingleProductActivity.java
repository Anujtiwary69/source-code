package co.stutzen.shopzen;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;

import org.json.JSONArray;
import org.json.JSONObject;

import co.stutzen.shopzen.adapters.ColorAdapter;
import co.stutzen.shopzen.adapters.DropDownAdapter;
import co.stutzen.shopzen.adapters.GalleryAdapter;
import co.stutzen.shopzen.adapters.GridviewAdapter;
import co.stutzen.shopzen.adapters.HomeTopAdapter;
import co.stutzen.shopzen.adapters.ProductAdapter;
import co.stutzen.shopzen.bo.ColorBO;
import co.stutzen.shopzen.bo.ProductBO;
import co.stutzen.shopzen.bo.VariationBO;
import co.stutzen.shopzen.constants.AppConstants;
import co.stutzen.shopzen.constants.Connection;
import co.stutzen.shopzen.custom.ExpandableHeightGridView;
import co.stutzen.shopzen.database.DBController;

public class SingleProductActivity extends Activity {

	private Gallery gallery;
	private TextView info,delivery,addcart,addcart1,colortext,produtname,productprice;
	private LinearLayout infolay,delivlay;
	private int selectedImagePosition = 0;
	private List<String> gallerylist;
	private View infovw,delivw;
	private Spinner sizespinner;
	private ExpandableHeightGridView gridvw;
	private LinearLayout drawItem;
	private GalleryAdapter galImageAdapter;
	private int cartvalue;
	private Dialog open;
	private FrameLayout colorframe;
	private ListView colorlistvw;
	private ArrayList<ColorBO> colorlist;
	private ColorAdapter coloradap;
	private ExpandableHeightGridView colorgrid;
	private LinearLayout colorlay;
	private DBController dbCon;
	private String prosize,proid;
	private ImageView menuicon;
	private ArrayList<String> sizelist;
	private int qtyy;
	private ArrayList<ProductBO> cartlist;
	private String colorcode;
	private String proname;
	private double proprice;
	private String proimage;
	private ArrayList<ProductBO> gridlist;
	private GridviewAdapter adapter;
	private Client client;
	private ArrayList<TextView> galleryHighlight;
	private LinearLayout highlightlay;
	private TextView description;
	private ArrayList<VariationBO> variationlist;
	private int variationId;
	private String protype;
	private String sizeval;
	private String colorval;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		client = new Client(AppConstants.ALGOLIA_APP_ID, AppConstants.ALGOLIA_APP_KEY);
		setContentView(R.layout.single_product_layout);
		galleryHighlight = new ArrayList<TextView>();
		dbCon = new DBController(SingleProductActivity.this);
		LinearLayout backclick = (LinearLayout) findViewById(R.id.backclick);
		backclick.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onBackPressed();
			}
		});
		gallery = (Gallery) findViewById(R.id.gallery);
		highlightlay = (LinearLayout) findViewById(R.id.highlightlay);
		infovw=(View)findViewById(R.id.infovw);
		delivw=(View)findViewById(R.id.delivervw);
		info=(TextView)findViewById(R.id.info);
		delivery=(TextView)findViewById(R.id.deliver);
		description =(TextView)findViewById(R.id.description);
		gridvw=(ExpandableHeightGridView)findViewById(R.id.gridvw);
		produtname=(TextView)findViewById(R.id.sname);
		productprice=(TextView)findViewById(R.id.sprice);
		colorframe=(FrameLayout)findViewById(R.id.colorframe);
		colortext=(TextView)findViewById(R.id.color);
		infolay=(LinearLayout)findViewById(R.id.infolayout);
		delivlay=(LinearLayout)findViewById(R.id.delivlayout);
		sizespinner=(Spinner)findViewById(R.id.sizespinner);
		addcart=(TextView)findViewById(R.id.addcart);
		addcart1=(TextView)findViewById(R.id.addcart1);
		colorlistvw=(ListView)findViewById(R.id.colorlist);
		colorlay=(LinearLayout)findViewById(R.id.colorlay);
		colorgrid=(ExpandableHeightGridView)findViewById(R.id.colorgrid);
		
		cartlist=new ArrayList<ProductBO>();
		GradientDrawable drawable1 = (GradientDrawable) colortext.getBackground();
		colorcode="#dedede";
		colortext.setText("\\");
		drawable1.setColor(Color.parseColor(colorcode));

		produtname.setText(getIntent().getStringExtra("name"));
		productprice.setText("Rs " + String.format("%.0f", getIntent().getDoubleExtra("price", 0.00)) + "");
		proid = getIntent().getIntExtra("id", 0)+"";
		proname = getIntent().getStringExtra("name");
		proprice = getIntent().getDoubleExtra("price", 0.00);
		proimage = getIntent().getStringExtra("image");
		protype = getIntent().getStringExtra("type");
		gridlist=new ArrayList<ProductBO>();
		float scalefactor1 =getResources().getDisplayMetrics().density * 154;
		int number1 = getWindowManager().getDefaultDisplay().getWidth()-48;
		int columns1 = (int) (((float) number1) / scalefactor1);
		gridvw.setNumColumns(columns1);

		//getProductData();
		gridvw.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				// TODO Auto-generated method stub
				adapter.clickchild(arg2);
				adapter.notifyDataSetChanged();
				proid = gridlist.get(arg2).getId()+"";
				proname = gridlist.get(arg2).getName();
				proprice = gridlist.get(arg2).getAmount();
				proimage = gridlist.get(arg2).getImage();
				protype = gridlist.get(arg2).getType();
				produtname.setText(gridlist.get(arg2).getName() + "");
				productprice.setText("Rs " + String.format("%.0f",gridlist.get(arg2).getAmount()) + "");
				GetProductTask task = new GetProductTask();
				task.execute(AppConstants.PRODUCT_RETIREVE_API+proid+"?"+AppConstants.cust_keysecret);
			}
		});
		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SingleProductActivity.this, ZoomingActivity.class);
				intent.putExtra("position", arg2);
				for(int i=0; i<gallerylist.size(); i++){
					intent.putExtra("image"+i, gallerylist.get(i));
				}
				intent.putExtra("size", gallerylist.size());
				intent.putExtra("name", proname);
				startActivity(intent);
			}
		});
	 
		
	addcart.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			boolean type = true;
			if (!protype.equalsIgnoreCase("simple")) {
				if (variationId == 0)
					type = false;
			}
			int cartcheck = 0;
			cartlist = dbCon.getCartList();
			if (cartlist != null && cartlist.size() > 0) {
				for (int k = 0; k < cartlist.size(); k++) {
					if (protype.equalsIgnoreCase("simple")) {
						if (cartlist.get(k).getId() == Integer.parseInt(proid)) {
							cartcheck = 1;
						}
					} else {
						if ((cartlist.get(k).getId() == Integer.parseInt(proid)) && (cartlist.get(k).getVariationId() == variationId)) {
							cartcheck = 1;
						}
					}
				}
				if (cartcheck == 1) {
					Toast.makeText(getApplicationContext(), "Product in cart already", Toast.LENGTH_SHORT).show();
				} else {
					if (type) {
						dbCon.insertCart(Integer.parseInt(proid), proname, proprice + "", 1, proimage, variationId, protype, sizeval, colorval);
						Toast.makeText(getApplicationContext(), "Product added to cart", Toast.LENGTH_SHORT).show();
					} else {
						Snackbar.make(produtname, "Please select color and size.", Snackbar.LENGTH_LONG)
								.setAction("Action", null).show();
					}
				}
			} else {
				if (type) {
					dbCon.insertCart(Integer.parseInt(proid), proname, proprice + "", 1, proimage, variationId, protype, sizeval, colorval);
					Toast.makeText(getApplicationContext(), "Product added to cart", Toast.LENGTH_SHORT).show();
				} else {
					Snackbar.make(produtname, "Please select color and size.", Snackbar.LENGTH_LONG)
							.setAction("Action", null).show();
				}
			}
		}
	});
	addcart1.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
			addcart.performClick();
		}
	});
	info.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			infovw.setBackgroundColor(getResources().getColor(R.color.colorAccent));
			delivw.setBackgroundColor(Color.parseColor("#ededed"));
			infolay.setVisibility(View.VISIBLE);
			delivlay.setVisibility(View.GONE);

		}
	});

	delivery.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			infovw.setBackgroundColor(Color.parseColor("#ededed"));
			delivw.setBackgroundColor(getResources().getColor(R.color.colorAccent));
			delivlay.setVisibility(View.VISIBLE);
			infolay.setVisibility(View.GONE);
		}
	});

	colortext.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			try {
				float scalefactor = getResources().getDisplayMetrics().density * 30;
				int number = getWindowManager().getDefaultDisplay().getWidth() - 32;
				int columns = (int) (((float) number) / (float) scalefactor);
				int cal = ((columns - 1) * 10);
				int finalcal = (int) (((float) number - cal) / (float) scalefactor);
				colorgrid.setNumColumns(finalcal);
				if (colorlay.getVisibility() == View.GONE) {
					coloradap = new ColorAdapter(getApplicationContext(), R.layout.color_list_item, colorlist);
					colorgrid.setAdapter(coloradap);
					String getcolor = getcolorcode();
					for (int k = 0; k < colorlist.size(); k++) {
						if (colorlist.get(k).getCatcolor().equals(getcolor)) {
							coloradap.setClickedChild1(k);
							coloradap.notifyDataSetChanged();
						}
					}
					colorgrid.setExpanded(true);
					expand(colorlay);
				} else {
					collapse(colorlay);
				}
			}catch (Exception e) {
			}
		}
	});

		colorgrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				// TODO Auto-generated method stub
				variationId = 0;
				coloradap.setClickedChild1(arg2);
				coloradap.notifyDataSetChanged();
				GradientDrawable drawable = (GradientDrawable) colortext.getBackground();
				colortext.setText("");
				colorcode = colorlist.get(arg2).getCatcolor();
				drawable.setColor(Color.parseColor(colorcode));
				String size = sizelist.get(sizespinner.getSelectedItemPosition());
				for (int i = 0; i < variationlist.size(); i++) {
					if (variationlist.get(i).getColor().equalsIgnoreCase(colorcode) && variationlist.get(i).getSize().equalsIgnoreCase(size)) {
						variationId = variationlist.get(i).getId();
						proprice = (variationlist.get(i).getPrice() != null && variationlist.get(i).getPrice().trim().length() > 0) ? Double.parseDouble(variationlist.get(i).getPrice()) : proprice;
						productprice.setText("Rs " + String.format("%.0f", proprice) + "");
						sizeval = size;
						colorval = colorcode;
					}
				}
				Log.i("variationId", variationId + "");
			}
		});

		sizespinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
				variationId = 0;
				String size = sizelist.get(sizespinner.getSelectedItemPosition());
				colorlist = new ArrayList<ColorBO>();
				for (int j = 0; j < variationlist.size(); j++) {
					if (variationlist.get(j).getSize().equalsIgnoreCase(size)) {
						ColorBO bo = new ColorBO();
						bo.setCatcolor(variationlist.get(j).getColor());
						colorlist.add(bo);
					}
				}
				coloradap = new ColorAdapter(getApplicationContext(), R.layout.color_list_item, colorlist);
				colorgrid.setAdapter(coloradap);
				GradientDrawable drawable1 = (GradientDrawable) colortext.getBackground();
				colorcode="#dedede";
				colortext.setText("\\");
				drawable1.setColor(Color.parseColor(colorcode));
				Log.i("variationId", variationId + "");
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});

		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				selectedImagePosition = pos;
				for (int i = 0; i < galleryHighlight.size(); i++) {
					galleryHighlight.get(i).setBackgroundColor(Color.parseColor("#CFCFCF"));
				}
				galleryHighlight.get(selectedImagePosition).setBackgroundColor(Color.parseColor("#65ADDD"));
			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});
		GetProductTask task = new GetProductTask();
		task.execute(AppConstants.PRODUCT_RETIREVE_API+proid+"?"+AppConstants.cust_keysecret);
	}

	public String getcolorcode(){
		return colorcode;
	}
	
	public static void collapse(final View v) {
	    final int initialHeight = v.getMeasuredHeight();

	    Animation a = new Animation()
	    {
	        @Override
	        protected void applyTransformation(float interpolatedTime, Transformation t) {
	            if(interpolatedTime == 1){
	                v.setVisibility(View.GONE);
	            }else{
	                v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
	                v.requestLayout();
	            }
	        }

	        @Override
	        public boolean willChangeBounds() {
	            return true;
	        }
	    };
	a.setDuration(300);
	v.startAnimation(a);
	}

	public static void expand(final View v) {
	    v.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	    final int targetHeight = v.getMeasuredHeight();
		v.getLayoutParams().height = 1;
	    v.setVisibility(View.VISIBLE);
	    Animation a = new Animation() {
	        @Override
	        protected void applyTransformation(float interpolatedTime, Transformation t) {
	            v.getLayoutParams().height = interpolatedTime == 1
	                    ? LayoutParams.WRAP_CONTENT
	                    : (int)((targetHeight) * interpolatedTime);
	            v.requestLayout();
	        }

	        @Override
	        public boolean willChangeBounds() {
	            return true;
	        }
	    };
	    a.setDuration(300);
	    v.startAnimation(a);
	}

	public void getProductData() {
		Index index = client.initIndex("shopzen_products");
		Query query = new Query();
		query.setFacets("*");
		query.setHitsPerPage(4);
		// query.setFilters("brand:\"Apple\" AND brand:\"Amazon\"");
		index.searchAsync(query, new CompletionHandler() {
			@Override
			public void requestCompleted(JSONObject jsonObject, AlgoliaException e) {
				if (e == null) {
					try {
						JSONArray array = jsonObject.getJSONArray("hits");
						if (array.length() > 0) {
							gridlist = new ArrayList<ProductBO>();
							for (int i = 0; i < array.length(); i++) {
								JSONObject singleObj = array.getJSONObject(i);
								gridlist.add(new ProductBO(singleObj.getInt("objectID"), singleObj.getString("name"), singleObj.getString("image"), singleObj.getInt("rating"), singleObj.getInt("popularity"), singleObj.getDouble("price"), false, 10.5, singleObj.getString("type")));
							}
							adapter=new GridviewAdapter(SingleProductActivity.this, R.layout.row_grid,gridlist);
							gridvw.setAdapter(adapter);
							gridvw.setExpanded(true);
						} else {
							Snackbar.make(gridvw, "No products found.", Snackbar.LENGTH_LONG)
									.setAction("Action", null).show();
						}
					} catch (Exception e1) {
						e1.printStackTrace();
						Snackbar.make(gridvw, "Error in network. Please check your internet connection.", Snackbar.LENGTH_LONG)
								.setAction("Action", null).show();
					}
				} else {
					e.printStackTrace();
				}
			}
		});
	}

	private class GetProductTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			Log.i("GetProductTask", "started");
		}

		protected String doInBackground(String... param) {
			Log.i("GetProductTask", param[0]);
			String response = null;
			try {
				Connection connection = new Connection();
				response = connection.connStringResponse(param[0], "");
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return response;
		}

		protected void onPostExecute(String resp) {
			Log.i("single product resp", resp + "");
			if(resp != null){
				try{
					JSONObject obj = new JSONObject(resp);
					description.setText(obj.getString("description"));
					JSONArray imgs = obj.getJSONArray("images");
					gallerylist = new ArrayList<String>();
					highlightlay.removeAllViews();
					galleryHighlight = new ArrayList<TextView>();
					for(int i=0; i<imgs.length(); i++){
						View lay = getLayoutInflater().inflate(R.layout.gallery_highlight, null);
						TextView txt1 = (TextView) lay.findViewById(R.id.txt1);
						highlightlay.addView(lay);
						galleryHighlight.add(txt1);
						if(i == 0) {
							txt1.setBackgroundColor(Color.parseColor("#65ADDD"));
							proimage = imgs.getJSONObject(i).getString("src");
						}else{
							txt1.setBackgroundColor(Color.parseColor("#CFCFCF"));
						}
						gallerylist.add(imgs.getJSONObject(i).getString("src"));
					}
					proid = obj.getInt("id")+"";
					proname = obj.getString("name");
					proprice = obj.getDouble("price");
					protype = obj.getString("type");
					galImageAdapter = new GalleryAdapter(SingleProductActivity.this, R.layout.gallery_item, gallerylist);
					gallery.setAdapter(galImageAdapter);
					produtname.setText(proname + "");
					productprice.setText("Rs " + String.format("%.0f", proprice) + "");
					if(!protype.equalsIgnoreCase("simple")) {
						variationlist = new ArrayList<VariationBO>();
						sizelist = new ArrayList<String>();
						colorlist = new ArrayList<ColorBO>();
						if (obj.has("attributes") && obj.getString("attributes") != null) {
							JSONArray jsonArray = new JSONArray(obj.getString("attributes"));
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject attrObj = jsonArray.getJSONObject(i);
								if (attrObj.getString("name").equalsIgnoreCase("Size")) {
									JSONArray respsize = attrObj.getJSONArray("options");
									for (int j = 0; j < respsize.length(); j++)
										sizelist.add(respsize.getString(j));
								}
								if (attrObj.getString("name").equalsIgnoreCase("Color")) {
									JSONArray respsize = attrObj.getJSONArray("options");
									for (int j = 0; j < respsize.length(); j++) {
										ColorBO bo = new ColorBO();
										bo.setCatcolor(respsize.getString(j));
										colorlist.add(bo);
									}
								}
							}
						}
						if (colorlist.size() > 0)
							colortext.setVisibility(View.VISIBLE);
						else
							colortext.setVisibility(View.GONE);
						if (sizelist.size() > 0) {
							DropDownAdapter spinneradapter = new DropDownAdapter(SingleProductActivity.this, R.layout.row, sizelist);
							sizespinner.setAdapter(spinneradapter);
						} else {
							sizespinner.setVisibility(View.GONE);
						}
						if (obj.has("variations") && obj.getString("variations") != null) {
							JSONArray variatnAry = new JSONArray(obj.getString("variations"));
							for (int i = 0; i < variatnAry.length(); i++) {
								JSONObject varObj = variatnAry.getJSONObject(i);
								VariationBO bo = new VariationBO();
								bo.setId(varObj.getInt("id"));
								bo.setPrice(varObj.getString("price"));
								ArrayList<String> images = new ArrayList<String>();
								for (int j = 0; j < varObj.getJSONArray("image").length(); j++) {
									JSONObject imgObj = varObj.getJSONArray("image").getJSONObject(j);
									images.add(imgObj.getString("src"));
								}
								JSONArray attrs = varObj.getJSONArray("attributes");
								for (int j = 0; j < attrs.length(); j++) {
									JSONObject arObj = attrs.getJSONObject(j);
									if (arObj.getString("name").equalsIgnoreCase("color"))
										bo.setColor(arObj.getString("option"));
									if (arObj.getString("name").equalsIgnoreCase("size"))
										bo.setSize(arObj.getString("option"));
								}
								variationlist.add(bo);
							}
						}
					}else{
						sizespinner.setVisibility(View.GONE);
						colortext.setVisibility(View.GONE);
					}
				}catch (Exception e){
					e.printStackTrace();
					Snackbar.make(produtname, "Error in network. Please check your internet connection.", Snackbar.LENGTH_LONG)
							.setAction("Action", null).show();
				}
			}else{
				Snackbar.make(produtname, "Error in network. Please check your internet connection.", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
			}
		}
	}

	public void onBackPressed() {
		super.onBackPressed();
	}
}
