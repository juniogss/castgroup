package br.com.junio.castgroup.common.adapter;

import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;

import br.com.junio.castgroup.R;
import br.com.junio.castgroup.common.model.Category;
import br.com.junio.castgroup.main.presenter.categories.CategoryListener;

public class CategoryItem extends Item<GroupieViewHolder> {

    private final Category category;
    private final CategoryListener listener;

    public CategoryItem(Category category, CategoryListener listener) {
        this.category = category;
        this.listener = listener;
    }

    @Override
    public void bind(@NonNull GroupieViewHolder viewHolder, int position) {
        TextView tvDesc = viewHolder.itemView.findViewById(R.id.tvDesc);
        TextView tvCode = viewHolder.itemView.findViewById(R.id.tvCode);

        ImageButton btEdit = viewHolder.itemView.findViewById(R.id.btEdit);
        ImageButton btDelete = viewHolder.itemView.findViewById(R.id.btDelete);

        tvDesc.setText(category.getDesc());
        tvCode.setText(String.format("Código: %1$s", category.getCode()));

        btEdit.setOnClickListener(view -> listener.onEditClick(category));
        btDelete.setOnClickListener(view -> listener.onDeleteClick(category));
    }

    @Override
    public int getLayout() {
        return R.layout.category_item;
    }
}
