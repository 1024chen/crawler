package com.example.crawler.service;

import com.example.crawler.model.http.request.ImageBo;
import com.example.crawler.model.http.request.NewAndSpecSaveBo;
import com.example.crawler.model.http.response.NewsResponse;
import com.example.crawler.model.http.response.OuterBo;
import com.example.crawler.model.http.response.ResponseBo;
import com.example.crawler.util.HttpUtil;
import com.example.crawler.util.ImageUtil;
import com.example.crawler.util.TimeDateUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SendService {
    @Resource
    private CrawlerService crawlerService;
    @Resource
    private ObjectMapper objectMapper;
    @Value("${backend.location}")
    private String location;

    public void sendSpecialCase() {
        NewsResponse response = getLastSpecialCase();
        if (response == null) {
            log.error("获取特色案例历史数据失败！");
            return;
        }
        if (response.getIsDeleted().equals("1") && response.getSpecialCaseAnnTime() == null) {
            updateInfo(crawlerService.getSpecialCase());
        } else {
            updateInfo(filterSpecialCase(response.getSpecialCaseAnnTime(),crawlerService.getSpecialCase()));
        }
    }

    public void sendAlliance() {
        NewsResponse response = getLastAlliance();
        if (response == null) {
            log.error("获取联盟动态历史数据失败！");
            return;
        }
        if (response.getIsDeleted().equals("1") && response.getNewsSrcAnnTime() == null) {
            updateInfo(crawlerService.getAlliance());
        } else {
            updateInfo(filterAlliance(response.getNewsSrcAnnTime(),crawlerService.getAlliance()));
        }
    }

    public void sendAllGovFiles() {
        updateInfo(crawlerService.getAllGovFiles());
    }

    public void sendSomeGovFiles() {
        NewsResponse response = getLastPolicy();
        if (response == null) {
            log.error("获取政策历史数据失败！");
            return;
        }
        if (response.getIsDeleted().equals("1") && response.getNewsSrcAnnTime() == null) {
            updateInfo(crawlerService.getSomeGovFiles());
        } else {
            updateInfo(filterFiles(response.getPolicySrcAnnTime(), crawlerService.getSomeGovFiles()));
        }
    }

    public void sendAllMediaFocus() {
        updateInfo(crawlerService.getAllMediaFocus());
    }

    public void sendSomeMediaFocus() {
        NewsResponse response = getLastMediaFocus();
        if (response == null) {
            log.error("获取媒体聚焦历史数据失败！");
            return;
        }
        if (response.getIsDeleted().equals("1") && response.getNewsSrcAnnTime() == null) {
            updateInfo(crawlerService.getSomeMediaFocus());
        } else {
            updateInfo(filterFocus(response.getNewsSrcAnnTime(),crawlerService.getSomeMediaFocus()));
        }
    }

    private void updateInfo(List<NewAndSpecSaveBo> data) {
        String url = location + "/updateInfoBatch";
        try {
            HttpUtil.post(url,objectMapper.writeValueAsString(data));
        } catch (JsonProcessingException e) {
            log.error("数据保存失败,{}",e.getMessage(),e);
        }
    }

    private NewsResponse getLastPolicy() {
        return getNewsResponseFromBackend(location + "/getLastPolicy");
    }

    private NewsResponse getLastAlliance() {
        return getNewsResponseFromBackend(location + "/getLastAlliance");
    }

    private NewsResponse getLastSpecialCase() {
        return getNewsResponseFromBackend(location + "/getLastSpecialCase");
    }

    private NewsResponse getLastMediaFocus() {
        return getNewsResponseFromBackend(location + "/getLastMediaFocus");
    }

    public String sendImageToLowCode(String coverUrl){
        String url = location + "/uploadImage";
        String result = "";
        ImageBo imageBo = ImageUtil.getCoverImage(coverUrl);
        try {
            String response = HttpUtil.post(url,objectMapper.writeValueAsString(imageBo));
            result = objectMapper.readValue(response, new TypeReference<OuterBo<ResponseBo>>() {
            }).getData().getResult();
        } catch (JsonProcessingException e) {
            log.error("图片对象转换失败,{}",e.getMessage(),e);
        }
        return result;
    }

    @Nullable
    private NewsResponse getNewsResponseFromBackend(String url) {
        String response = HttpUtil.get(url);
        NewsResponse newsResponse = null;
        try {
            newsResponse = objectMapper.readValue(response, new TypeReference<OuterBo<NewsResponse>>() {
            }).getData();
        } catch (JsonProcessingException e) {
            log.error("数据转换失败{}",e.getMessage(),e);
        }
        return newsResponse;
    }

    private List<NewAndSpecSaveBo> filterSpecialCase(String oldTime, List<NewAndSpecSaveBo> specialCase) {
        List<NewAndSpecSaveBo> filteredList = getFilteredList(oldTime, specialCase,1);
        filteredList.forEach(a -> a.getUpdate().setSpecialCaseListChart(
                sendImageToLowCode(a.getUpdate().getSpecialCaseListChart())));
        return filteredList;
    }

    /**
     * 1.specialCase
     * 2.Alliance
     * 3.Files
     * 4.Focus
     */
    @NotNull
    private static List<NewAndSpecSaveBo> getFilteredList(String oldTime, List<NewAndSpecSaveBo> saveBoList, int type) {
        return saveBoList.stream()
                .filter(a -> TimeDateUtil.isDateAfter(oldTime, switch (type){
                    case 1 -> a.getUpdate().getSpecialCaseAnnTime();
                    case 3 -> a.getUpdate().getPolicySrcAnnTime();
                    default -> a.getUpdate().getNewsSrcAnnTime();
                })).toList();
    }

    private List<NewAndSpecSaveBo> filterAlliance(String oldTime, List<NewAndSpecSaveBo> alliance) {
        var filteredList = getFilteredList(oldTime, alliance,2);
        filteredList.forEach(a -> a.getUpdate().setSpecialCaseListChart(
                sendImageToLowCode(a.getUpdate().getSpecialCaseListChart())));
        return filteredList;
    }

    private List<NewAndSpecSaveBo> filterFiles(String oldTime, List<NewAndSpecSaveBo> filesList) {
        return getFilteredList(oldTime, filesList,3);
    }

    private List<NewAndSpecSaveBo> filterFocus(String oldTime, List<NewAndSpecSaveBo> fucosList) {
        return getFilteredList(oldTime, fucosList,4);
    }
}

