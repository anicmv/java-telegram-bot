package com.github.anicmv.Inline.impl;

import cn.hutool.core.io.resource.ResourceUtil;
import com.github.anicmv.Inline.InlineQueryResultProvider;
import com.github.anicmv.contant.BotConstant;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author anicmv
 * @date 2025/3/30 00:48
 * @description 编程语言排行榜
 */
@Slf4j
@Component
public class TopProgrammingLanguagesResultProvider implements InlineQueryResultProvider {

    @Override
    public String getSortId() {
        return BotConstant.N_8;
    }

    @Override
    public InlineQueryResult createResult(InlineQuery inlineQuery) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        String dateStr = sdf.format(new Date());
        String filename = "top-" + dateStr + ".txt";

        // 资源路径：项目 src/main/resources 下的 programmer 文件夹
        String resourcePath = "programmer/" + filename;

        // 尝试通过 ResourceUtil 获取资源 URL
        URL resource = ResourceUtil.getResource(resourcePath);

        Path filePath = null;
        if (resource != null) {
            log.info("资源存在");
            // 若资源存在，判断协议是否为 file
            if ("file".equals(resource.getProtocol())) {
                try {
                    filePath = Paths.get(resource.toURI());
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
        // 如果 filePath 为空，则使用相对路径：src/main/resources/programmer/filename
        if (filePath == null) {
            log.info("filepath不存在");
            filePath = Paths.get("src/main/resources", resourcePath);
        }

        String programmerTxt = null;

        // 判断文件是否存在
        if (Files.exists(filePath)) {
            try {
                // 读取文件内容
                programmerTxt = Files.readString(filePath, Charset.defaultCharset());
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        } else {
            log.info("文件不存在");
            // 文件不存在则调用 topProgrammer() 方法生成字符串内容
            programmerTxt = topProgrammer();
            if (programmerTxt == null) {
                programmerTxt = "出错了...";
                log.info("出错了...");
            }
            // 写入文件前，创建必要的目录
            try {
                Files.createDirectories(filePath.getParent());
                Files.writeString(filePath, programmerTxt, Charset.defaultCharset(),
                        StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }


        InputTextMessageContent content = InputTextMessageContent.builder()
                .messageText("<pre>" + programmerTxt + "</pre>")
                .parseMode("HTML")
                .build();


        return InlineQueryResultArticle.builder()
                .id(getSortId())
                .title("编程语言排行榜")
                .thumbnailUrl("https://jpg.moe/i/6oalto65.jpeg")
                .inputMessageContent(content)
                .build();
    }

    private String topProgrammer() {
        // 目标URL地址
        String url = "https://www.tiobe.com/tiobe-index/";
        try {
            // 获取并解析HTML页面
            Document doc = Jsoup.connect(url).get();

            // 获取 id 为 "top20" 的表格
            Element tableElement = doc.getElementById("top20");
            if (tableElement == null) {
                System.out.println("未找到 id 为 'top20' 的表格！");
                return null;
            }

            // 获取所有行
            Elements rows = tableElement.select("tr");

            // 固定三个字段的宽度（这里可根据具体情况调整）
            int col1Width = 22;
            int col2Width = 9;
            int col3Width = 8;

            // 构建横线字符串
            String horizontalLine = "+" + "-".repeat(col1Width) + "+" + "-".repeat(col2Width) + "+" + "-".repeat(col3Width) + "+";

            // 使用 StringBuilder 拼接所有文本内容
            StringBuilder sb = new StringBuilder();
            sb.append(horizontalLine).append("\n");
            String format = "| %-" + (col1Width - 2) + "s | %-" + (col2Width - 2) + "s | %-" + (col3Width - 2) + "s |\n";
            sb.append(String.format(format,
                    "Language", "Ratings", "Change"));
            sb.append(horizontalLine).append("\n");

            // 遍历每一行（跳过表头行）
            for (int i = 1; i < rows.size(); i++) {
                Element row = rows.get(i);
                Elements columns = row.select("td");
                // 根据 Python 代码逻辑：取第5,6,7个<td>标签（索引从0开始计数）
                if (columns.size() >= 7) {
                    String language = columns.get(4).text();
                    String ratings = columns.get(5).text();
                    String change = columns.get(6).text();

                    sb.append(String.format(format,
                            language, ratings, change));
                }
            }
            sb.append(horizontalLine);

            return sb.toString();

        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
