package br.com.luizfp.segredosufsc.segredo.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.luizfp.segredosufsc.R;
import br.com.luizfp.segredosufsc.new_implementation.base.BaseFragment;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
@Deprecated
public class SegredosTabFragment extends BaseFragment implements
        TabLayout.OnTabSelectedListener {
    private static final int NUM_PAGES = 2;
    private static final String TAG = SegredosTabFragment.class.getSimpleName();
    @BindView(R.id.viewPager) ViewPager viewPager;

    public SegredosTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_segredos_tab, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        // ViewPager
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new SegredosTabsAdapter(getChildFragmentManager()));

        // Tabs
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        // Adiciona as tabs
        tabLayout.addTab(tabLayout.newTab().setText(R.string.text_todos));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.text_favoritos));
        // Listener para tratar eventos ao clique na tab
        tabLayout.setOnTabSelectedListener(this);
        // Se mudar o ViewPager atualiza o indicador na tab
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        return view;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        // Se alterar tab, atualiza o viewPager
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private class SegredosTabsAdapter extends FragmentPagerAdapter {

        public SegredosTabsAdapter(FragmentManager childFragmentManager) {
            super(childFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            if (position == 0) {
                bundle.putString(SegredosFragment.EXTRA_TIPO, SegredosFragment.TIPO_TODOS);
            } else {
                bundle.putString(SegredosFragment.EXTRA_TIPO, SegredosFragment.TIPO_FAVORITOS);
            }

            Fragment fragment = new SegredosFragment();
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}

