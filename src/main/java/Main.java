import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String srcFolder = "c:\\MY\\123\\src_img";  // Каталог источник
        String dstFolder = "c:\\MY\\123\\dst_img";  // каталог назначения

        int cpuCount = Runtime.getRuntime().availableProcessors();          // Количество процессоров ПК

        File srcDir = new File(srcFolder);                                  // Объект каталога с файлами
        File[] files = srcDir.listFiles();                                  // Список всех файлов в заданном каталоге
        List<File[]> filesThead = splittingFilesArray(files, cpuCount);     // Разделенный список массивов на заданное количество
        List<ImagesResizer> imagesResizers = new ArrayList<>();             // Список объектов преобразования изображения


        // Выполнить изменение размера (сжатие) в разных потоках
        filesThead.stream()
                .forEach(partArr -> {
                    imagesResizers.add(new ImagesResizer(partArr, 0.1, dstFolder));
                    new Thread(imagesResizers.get(imagesResizers.size() - 1)).start();
                });
    }


    /**
     * Дробление массива на заданное число частей
     * @param files исходный массив фалов
     * @param quantityPart количество частей
     * @return
     */
    public static List<File[]> splittingFilesArray(File[] files, int quantityPart) {

        int qFiles = files.length;                                      // Общее кол-во файлов
        int qFilesInPart = (qFiles + quantityPart - 1) / quantityPart;  // Кол-во файлов в одной части разделенного массива
        List<File[]> partArrays = new ArrayList<>(quantityPart);        // Список частей массива

        // Инициализируем массивы в списке
        for(int i = 0; i < quantityPart; i++)
            partArrays.add(new File[qFilesInPart]);

        // Заполняем
        for (int j = 0; j < qFilesInPart; j++)
        {
            for (int i = 0; i < quantityPart; i++){
                if(qFiles > 0) {
                    qFiles--;
                    partArrays.get(i)[j] = files[qFiles];
                }
                else break;
            }
        }

        return partArrays;
    }
}
