package com.module.core.extension

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
    Parameterized Types <P, R>:
        P: Kiểu dữ liệu được sử dụng để cập nhật tiến trình.
        R: Kiểu dữ liệu của kết quả trả về từ tác vụ nền.

    Parameters của Hàm:
        onPreExecute: () -> Unit: Một lambda không có tham số và không trả về giá trị (Unit). Đây là nơi để thiết lập hoặc
    khởi tạo trước khi tác vụ nền bắt đầu, tương tự như phương thức onPreExecute của AsyncTask.
        doInBackground: suspend (suspend (P) -> Unit) -> R: Một lambda suspend nhận một lambda suspend khác làm tham số
        và trả về một giá trị kiểu R. Tham số lambda suspend này (suspend (P) -> Unit) dùng để cập nhật tiến trình và được
        gọi từ bên trong tác vụ nền.
        onPostExecute: (R) -> Unit: Một lambda nhận kết quả của tác vụ nền và không trả về giá trị (Unit). Được gọi sau khi
        tác vụ nền hoàn thành, tương tự như onPostExecute trong AsyncTask.
        onProgressUpdate: (P) -> Unit: Một lambda để cập nhật giao diện người dùng dựa trên tiến trình được báo cáo bởi tác vụ nền.

    Execution Flow:
        onPreExecute() được gọi đầu tiên để thực hiện các thiết lập ban đầu.
        launch: Khởi chạy một coroutine trong CoroutineScope gọi hàm.
        withContext(Dispatchers.IO): Đổi sang Dispatcher cho I/O để thực hiện tác vụ nặng như đọc/ghi cơ sở dữ liệu hoặc mạng.
        doInBackground: Thực hiện tác vụ nền. Lambda doInBackground được thiết kế để cho phép cập nhật tiến trình bằng cách
        sử dụng hàm withContext(Dispatchers.Main) { onProgressUpdate(it) }, cho phép cập nhật giao diện người dùng từ luồng nền.
        onPostExecute(result): Gọi khi tác vụ hoàn thành, nhận kết quả result và thực hiện các hành động cuối cùng
        trên giao diện người dùng.
 */
fun <P, R> CoroutineScope.executeAsyncTask(
    onPreExecute: () -> Unit,
    doInBackground: suspend (suspend (P) -> Unit) -> R,
    onPostExecute: (R) -> Unit,
    onProgressUpdate: (P) -> Unit
) = launch {
    onPreExecute.invoke()

    val result = withContext(Dispatchers.IO) {
        doInBackground {
            withContext(Dispatchers.Main) { onProgressUpdate.invoke(it) }
        }
    }

    onPostExecute.invoke(result)
}