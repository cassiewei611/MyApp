package dk.itu.shopping;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.graphics.drawable.BitmapDrawable;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {

    private static final ItemsDB itemsDB = new ItemsDB();

    // Existing MutableLiveData for UI state
    private final MutableLiveData<ShoppingUiState> uiState = new MutableLiveData<>(ShoppingUiState.getEmpty());

    // New MutableLiveData for the image
    private final MutableLiveData<Bitmap> imageLiveData = new MutableLiveData<>();

    // Getter for the image LiveData

    public LiveData<ShoppingUiState> getUiState() {
        return uiState;
    }

    public void awaitInit() {
        itemsDB.awaitInit();
        updateUiState();
    }

    public void onAddItemClick(TextView itemWhat, TextView itemWhere, TextView itemDescription,TextView itemStartDate,TextView itemEndDate, ImageView itemImage,FragmentActivity activity) {
        String whatS = itemWhat.getText().toString().trim();
        String whereS = itemWhere.getText().toString().trim();
        String descriptionS = itemDescription.getText().toString().trim();
        String startDateS = itemStartDate.getText().toString().trim();
        String endDateS = itemEndDate.getText().toString().trim();

        Bitmap image = null;
        if (itemImage.getDrawable() instanceof BitmapDrawable) {
            image = ((BitmapDrawable) itemImage.getDrawable()).getBitmap();
        }
        if (!whatS.isEmpty() && !whereS.isEmpty() && !descriptionS.isEmpty() && !startDateS.isEmpty() && !endDateS.isEmpty() && image != null) {
            byte[] imageBytes = Item.scaleAndConvert(image);
            itemsDB.addItem(whatS, whereS, startDateS, endDateS, descriptionS, imageBytes);

            // 清除文本输入
            clearTextViews(itemWhat, itemWhere, itemStartDate, itemEndDate, itemDescription);

            // 清除图片视图
            clearImageView(itemImage);

            // 重要：确保更新UI状态
            updateUiState();

        } else
            showToast(activity);
    }


    private void clearTextViews(TextView... views) {
        for (TextView view : views) {
            view.setText("");
        }
    }

    private void clearImageView(ImageView imageView) {
        imageView.setImageResource(android.R.color.transparent);  // 设置为透明或默认图片
    }

    public void onDeleteItemClick(String what, FragmentActivity activity) {
        String message;
        if (containsItem(itemsDB.getValues(), what)) {
            itemsDB.removeItem(what);
            updateUiState();
            message = "Removed " + what;
        } else {
            message = what + " not found";
        }
        showToast(activity, message);
    }

    private boolean containsItem(List<Item> itemList, String what) {
        for (Item item : itemList) {
            if (item.getWhat().equals(what)) {
                return true;
            }
        }
        return false;
    }

    private void updateUiState() {
        uiState.setValue(new ShoppingUiState(itemsDB.getValues(), itemsDB.size()));

    }

    private void showToast(FragmentActivity activity) {
        Toast.makeText(activity, R.string.empty_toast, Toast.LENGTH_LONG).show();
    }

    private void showToast(FragmentActivity activity, CharSequence message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    public static class ShoppingUiState {
        final List<Item> itemList;
        final int itemListSize;

        public ShoppingUiState(List<Item> itemList, int itemListSize) {
            this.itemList = itemList;
            this.itemListSize = itemListSize;
        }

        public static ShoppingUiState getEmpty() {
            return new ShoppingUiState(new ArrayList<>(), 0);
        }
    }
}
