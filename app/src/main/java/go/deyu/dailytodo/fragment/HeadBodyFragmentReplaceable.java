package go.deyu.dailytodo.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by huangeyu on 15/10/27.
 */
public interface HeadBodyFragmentReplaceable {
    /**
     * do change HeadFragment
     *
     * @param headfragment fragment want to replace old fragment
     */
    public void changeHeadFragment(Fragment headfragment);
    /**
     * do change BodyFragment
     *
     * @param bodyfragment fragment want to replace old fragment
     */
    public void changeBodyFragment(Fragment bodyfragment) ;
}
