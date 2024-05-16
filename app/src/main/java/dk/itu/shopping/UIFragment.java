package dk.itu.shopping;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.graphics.drawable.BitmapDrawable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

public class UIFragment extends Fragment {
    private final static String IMAGE_INTENT = "android.media.action.IMAGE_CAPTURE";
    private static final int IMAGE_REQUEST = 2;

    private TextView newWhat, newStartDate, newEndDate, newWhere, newDescription;
    private ImageView newImage;
    private Bitmap imageBitmap;

    private Button addItem, listItems, newImage_button;
    private MainViewModel viewModel;

    private ItemsDB itemsDB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_ui, container, false);

        itemsDB = new ItemsDB();  // Assuming ItemsDB can be instantiated directly
        initializeViews(view);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        setupListeners();
        return view;
    }

    private void initializeViews(View view) {
        newWhat = view.findViewById(R.id.what_text);
        newStartDate = view.findViewById(R.id.start_date);
        newEndDate = view.findViewById(R.id.end_date);
        newWhere = view.findViewById(R.id.where_text);
        newDescription = view.findViewById(R.id.description);
        newImage = view.findViewById(R.id.image_view);
        newImage_button = view.findViewById(R.id.photo_button);
        addItem = view.findViewById(R.id.add_button);
        listItems = view.findViewById(R.id.list);
    }

    private void setupListeners() {
        listItems.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_UIFragment_to_ListFragment);
        });

        newImage_button.setOnClickListener(v -> {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(intent, IMAGE_REQUEST);
        });

        addItem.setOnClickListener(view -> viewModel.onAddItemClick(newWhat, newWhere, newDescription,newStartDate,newEndDate,newImage, getActivity()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_REQUEST) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            newImage.setVisibility(View.VISIBLE);
            newImage.setImageBitmap(imageBitmap);
        }
    }
}
