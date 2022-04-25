// Generated code from Butter Knife. Do not modify!
package com.ronnelrazo.physical_counting;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.viewpager.widget.ViewPager;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.material.tabs.TabLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class tab_from_ViewBinding implements Unbinder {
  private tab_from target;

  @UiThread
  public tab_from_ViewBinding(tab_from target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public tab_from_ViewBinding(tab_from target, View source) {
    this.target = target;

    target.tabs = Utils.findRequiredViewAsType(source, R.id.tabs, "field 'tabs'", TabLayout.class);
    target.pager = Utils.findRequiredViewAsType(source, R.id.pager, "field 'pager'", ViewPager.class);
    target.headerDetails = Utils.arrayFilteringNull(
        Utils.findRequiredViewAsType(source, R.id.types, "field 'headerDetails'", TextView.class), 
        Utils.findRequiredViewAsType(source, R.id.orgcode, "field 'headerDetails'", TextView.class), 
        Utils.findRequiredViewAsType(source, R.id.orgname, "field 'headerDetails'", TextView.class), 
        Utils.findRequiredViewAsType(source, R.id.farmcode, "field 'headerDetails'", TextView.class), 
        Utils.findRequiredViewAsType(source, R.id.farmname, "field 'headerDetails'", TextView.class), 
        Utils.findRequiredViewAsType(source, R.id.docDate, "field 'headerDetails'", TextView.class), 
        Utils.findRequiredViewAsType(source, R.id.auditDate, "field 'headerDetails'", TextView.class));
  }

  @Override
  @CallSuper
  public void unbind() {
    tab_from target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tabs = null;
    target.pager = null;
    target.headerDetails = null;
  }
}
