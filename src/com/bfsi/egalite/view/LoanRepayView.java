package com.bfsi.egalite.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.bfsi.egalite.main.BaseActivity;
import com.bfsi.egalite.pageindicators.BfsiViewPager;
import com.bfsi.egalite.pageindicators.TabPageIndicator;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.view.loan.repayment.RepayAgenda;
import com.bfsi.egalite.view.loan.repayment.RepayEntry;

public class LoanRepayView extends BaseActivity{

	private static String[] CONTENT = null;
	private BfsiViewPager mPager;
	private TabPageIndicator mPageIndicator;
	public static FragmentStatePagerAdapter adapter;
	public SparseArray<Fragment> mRegisteredFragments = new SparseArray<Fragment>();
	private View list_layout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		String agenda = getString(R.string.Agenda);
		String entry = getString(R.string.Entry);
		CommonContexts.dismissProgressDialog();
		CONTENT = new String[] { agenda, entry};
		CommonContexts.mMfi = ((MfiApplication) getApplicationContext());
		list_layout = getLayoutInflater().inflate(R.layout.tabs_layout,null);
		mPager = (BfsiViewPager) list_layout.findViewById(R.id.pager);
		mPageIndicator = (TabPageIndicator) list_layout.findViewById(R.id.indicator);

		adapter = new FragmentsAdapter(getSupportFragmentManager());

		mPager.setAdapter(adapter);
		mPager.setOffscreenPageLimit(0);

		mPageIndicator.setViewPager(mPager);

		mMiddleFrame.removeAllViews();
		mMiddleFrame.addView(list_layout);
	}
	class FragmentsAdapter extends FragmentStatePagerAdapter {
		
		public FragmentsAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

			int posi = mPageIndicator.getCurrentItem();
			
			if (posi == 0) {
				BaseActivity.mBtnLeft.setTag(R.drawable.home);
				BaseActivity.mBtnLeft.setImageResource(R.drawable.home);
				BaseActivity.mTitle.setText( R.string.screen_rpy_agenda);
				CommonContexts.CURRENT_SCREEN = CommonContexts.SCREEN_RP_AGENDA;
			}
			switch (position) {
			case 0:
				return RepayAgenda.newInstance(mPager);
			case 1:
				return RepayEntry.newInstance(mPageIndicator,mPager);
			default:
				return RepayAgenda.newInstance(mPager);
			}
		}
		@Override
		public CharSequence getPageTitle(int position) {
			return CONTENT[position % CONTENT.length].toUpperCase();
		}
		@Override
		public int getCount() {
			return CONTENT.length;
		}
		@Override
	    public Object instantiateItem(ViewGroup container, int position) {
	        Fragment fragment = (Fragment) super.instantiateItem(container, position);
	        mRegisteredFragments.put(position, fragment);
	        return fragment;
	    }

	    @Override
	    public void destroyItem(ViewGroup container, int position, Object object) {
	        mRegisteredFragments.remove(position);
	        super.destroyItem(container, position, object);
	    }

	    public Fragment getRegisteredFragment(int position) {
	        return mRegisteredFragments.get(position);
	    }
	}
	@Override
	protected void onRightAction() {
		super.onRightAction();

	}

	@Override
	protected void onLeftAction() {
		Object tag = BaseActivity.mBtnLeft.getTag();
		int id = tag == null ? -1 : (Integer) tag;
		if (id == R.drawable.home && mPager.getCurrentItem() == 0) {
			Intent cash = new Intent(LoanRepayView.this, HomeView.class);
			startActivity(cash);
			finish();
		} else if ((id == R.drawable.back || id==R.drawable.cancel)  && (mPager.getCurrentItem() == 1)) {
			super.onLeftAction();
		} else if ((id == R.drawable.imgsearch)  && (mPager.getCurrentItem() == 1)) {
			super.onLeftAction();
		} 
	}
		
	
	@Override
	public void onBackPressed() {
		handleBackPressed();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
		}
		return super.onKeyDown(keyCode, event);
	}

	private void handleBackPressed() {
		
			if(mPager.getCurrentItem()==0)
			{
				Intent cash = new Intent(LoanRepayView.this, HomeView.class);
				startActivity(cash);
				finish();
			}
			else if(mPager.getCurrentItem()==1)
			{
				RepayEntry fragment = (RepayEntry)mRegisteredFragments.get(mPager.getCurrentItem());
				fragment.myOnKeyDown();
			}
	}
}