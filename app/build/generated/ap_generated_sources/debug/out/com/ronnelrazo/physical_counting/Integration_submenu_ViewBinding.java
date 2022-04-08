// Generated code from Butter Knife. Do not modify!
package com.ronnelrazo.physical_counting;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class Integration_submenu_ViewBinding implements Unbinder {
  private Integration_submenu target;

  @UiThread
  public Integration_submenu_ViewBinding(Integration_submenu target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public Integration_submenu_ViewBinding(Integration_submenu target, View source) {
    this.target = target;

    target.breadcrumbs = Utils.arrayFilteringNull(
        Utils.findRequiredViewAsType(source, R.id.breadcrumb_header, "field 'breadcrumbs'", TextView.class), 
        Utils.findRequiredViewAsType(source, R.id.breadcrumb_orgname, "field 'breadcrumbs'", TextView.class), 
        Utils.findRequiredViewAsType(source, R.id.breadcrumb_orgcode, "field 'breadcrumbs'", TextView.class));
  }

  @Override
  @CallSuper
  public void unbind() {
    Integration_submenu target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.breadcrumbs = null;
  }
}
