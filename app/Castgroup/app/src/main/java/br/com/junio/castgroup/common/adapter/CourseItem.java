package br.com.junio.castgroup.common.adapter;

import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;

import br.com.junio.castgroup.R;
import br.com.junio.castgroup.common.model.Course;
import br.com.junio.castgroup.common.services.DateUtil;
import br.com.junio.castgroup.main.presenter.list.CourseListener;
import br.com.junio.castgroup.main.presenter.list.ListPresenter;

public class CourseItem extends Item<GroupieViewHolder> {

    private final Course course;
    private final CourseListener listener;
    private final ListPresenter presenter;

    public CourseItem(Course course, ListPresenter presenter, CourseListener listener) {
        this.course = course;
        this.listener = listener;
        this.presenter = presenter;
    }

    @Override
    public void bind(@NonNull GroupieViewHolder viewHolder, int position) {
        TextView tvDesc = viewHolder.itemView.findViewById(R.id.tvDesc);
        TextView tvDateStart = viewHolder.itemView.findViewById(R.id.tvDateStart);
        TextView tvDateEnd = viewHolder.itemView.findViewById(R.id.tvDateEnd);
        TextView tvStudents = viewHolder.itemView.findViewById(R.id.tvStudents);
        TextView tvCategory = viewHolder.itemView.findViewById(R.id.tvCategory);

        ImageButton btEdit = viewHolder.itemView.findViewById(R.id.btEdit);
        ImageButton btDelete = viewHolder.itemView.findViewById(R.id.btDelete);

        tvDesc.setText(course.getSubjectDesc());
        tvDateStart.setText(String.format("Início: %1$s", DateUtil.toString(course.getStartDate())));
        tvDateEnd.setText(String.format("Término: %1$s", DateUtil.toString(course.getEndDate())));
        tvStudents.setText(String.format("Alunos por turma: %1$s", course.getStudents() != null ? course.getStudents() : ""));
        tvCategory.setText(presenter.getCategoryBy(course.getFkCategory()));

        btEdit.setOnClickListener(view -> listener.onEditClick(course));
        btDelete.setOnClickListener(view -> listener.onDeleteClick(course));
    }

    @Override
    public int getLayout() {
        return R.layout.course_item;
    }
}
