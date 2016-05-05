package br.com.luizfp.segredosufsc.login.confirm_access_code;

import java.util.List;

import br.com.luizfp.segredosufsc.mvp.LoadingView;
import br.com.luizfp.segredosufsc.Curso;

/**
 * Created by luiz on 3/9/16.
 */
public interface ConfirmAccessCodeView extends LoadingView {
    void showLoadingCourses();
    void hideLoadingCourses();
    void fiilCoursesForSelection(List<Curso> coursesList);
    void fillLettersForSelection(char supostaInicialNome);
    void navigateToSegredos(char letterSelected);
}
