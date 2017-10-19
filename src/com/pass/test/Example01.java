package com.pass.test;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class Example01 {
	public interface BlogService {
		@GET("test/{param}")
		// �����{id} ��ʾ��һ������
		Call<ResponseBody> getBlog(@Path("param") String param);
	}

	public static void main(String[] args) throws IOException {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(
				"http://localhost:8080/pass/services/pass/").build();

		BlogService service = retrofit.create(BlogService.class);
		Call<ResponseBody> call = service.getBlog("abc");
		// �÷���OkHttp��call���һ��
		// ��ͬ���������Androidϵͳ�ص�����ִ�������߳�
		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call,
					Response<ResponseBody> response) {
				try {
					System.out.println(response.body().string());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Call<ResponseBody> call, Throwable t) {
				t.printStackTrace();
			}
		});
	}
}
