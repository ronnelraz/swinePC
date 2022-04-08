// Generated code from Butter Knife. Do not modify!
package com.ronnelrazo.physical_counting;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.material.button.MaterialButton;
import java.lang.IllegalStateException;
import java.lang.Override;

public class Farm_categories_ViewBinding implements Unbinder {
  private Farm_categories target;

  @UiThread
  public Farm_categories_ViewBinding(Farm_categories target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public Farm_categories_ViewBinding(Farm_categories target, View source) {
    this.target = target;

    target.logout = Utils.findRequiredViewAsType(source, R.id.logout, "field 'logout'", MaterialButton.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    Farm_categories target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.logout = null;
  }
}
