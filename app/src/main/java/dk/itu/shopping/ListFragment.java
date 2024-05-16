package dk.itu.shopping;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListFragment extends Fragment {

    // Model: Database of items
    MainViewModel viewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View v = inflater.inflate(R.layout.fragment_list, container, false);
        Button backButton = v.findViewById(R.id.back_button);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        // Set up recyclerview
        RecyclerView itemList = v.findViewById(R.id.listItems);
        itemList.setLayoutManager(new LinearLayoutManager(getActivity()));
        ItemAdapter mAdapter = new ItemAdapter();
        itemList.setAdapter(mAdapter);

        viewModel.getUiState().observe(getActivity(), itemsDB -> mAdapter.notifyDataSetChanged());


            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Navigation.findNavController(view).navigate(R.id.action_ListFragment_to_UIFragment);
                }
            });

        return v;
    }

    private class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView mWhatTextView, mWhereTextView, mDescriptionTextView, mStartDateTextView,mEndDateTextView,mNoView;
        private ImageView mImageView;

        public ItemHolder(View itemView) {
            super(itemView);
            mNoView = itemView.findViewById(R.id.item_no);
            mWhatTextView = itemView.findViewById(R.id.item_what);
            mWhereTextView = itemView.findViewById(R.id.item_where);
            mDescriptionTextView = itemView.findViewById(R.id.item_description);
            mStartDateTextView = itemView.findViewById(R.id.item_startDate);
            mEndDateTextView = itemView.findViewById(R.id.item_endDate);
            mImageView= itemView.findViewById(R.id.item_image);
            itemView.setOnClickListener(this);
        }

        public void bind(Item item, int position) {
            mWhatTextView.setText(item.getWhat());
            mWhereTextView.setText(item.getWhere());
            mStartDateTextView.setText(item.getStartDate());
            mEndDateTextView.setText(item.getEndDate());
            mDescriptionTextView.setText(item.getDescription());
            byte[] temp= item.getPict();
            if (temp != null)
                mImageView.setImageBitmap(item.ConvertByteArrayToBitmap(temp));
        }


        @Override
        public void onClick(View v) {
            // Delete item on click logic
            String what = (String) ((TextView) v.findViewById(R.id.item_what)).getText();
            mImageView.setImageBitmap(null);
            viewModel.onDeleteItemClick(what, getActivity());
        }
    }


    private class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View v = layoutInflater.inflate(R.layout.one_row, parent, false);
            return new ItemHolder(v);
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            Item item = viewModel.getUiState().getValue().itemList.get(position);
            holder.bind(item, position);
        }

        @Override
        public int getItemCount() {
            return viewModel.getUiState().getValue().itemListSize;
        }
    }
}