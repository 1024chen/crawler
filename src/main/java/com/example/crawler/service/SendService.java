package com.example.crawler.service;

import com.example.crawler.model.http.request.NewAndSpecSaveBo;
import com.example.crawler.model.http.response.NewsResponse;
import com.example.crawler.util.ImageUtil;
import com.example.crawler.util.TimeDateUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SendService {
    @Resource
    private CrawlerService crawlerService;

    public boolean sendSpecialCase() {
        NewsResponse response = getLastSpecialCase();
        if (response == null) {
            log.error("获取特色案例历史数据失败！");
            return false;
        }
        if (response.getIsDeleted().equals("1") && response.getSpecialCaseAnnTime() == null) {
            return updateInfo(crawlerService.getSpecialCase());
        } else {
            return updateInfo(filterSpecialCase(response.getSpecialCaseAnnTime(),crawlerService.getSpecialCase()));
        }
    }

    public boolean sendAlliance() {
        NewsResponse response = getLastAlliance();
        if (response == null) {
            log.error("获取联盟动态历史数据失败！");
            return false;
        }
        if (response.getIsDeleted().equals("1") && response.getNewsSrcAnnTime() == null) {
            return updateInfo(crawlerService.getAlliance());
        } else {
            return updateInfo(filterAlliance(response.getNewsSrcAnnTime(),crawlerService.getAlliance()));
        }
    }

    public boolean sendAllGovFiles() {
        return updateInfo(crawlerService.getAllGovFiles());
    }

    public void sendSomeGovFiles() {
        NewsResponse response = getLastPolicy();
        if (response == null) {
            log.error("获取政策历史数据失败！");
            return;
        }
        if (response.getIsDeleted().equals("1") && response.getNewsSrcAnnTime() == null) {
            crawlerService.getSomeGovFiles();
        } else {
            filterFiles(response.getPolicySrcAnnTime(), crawlerService.getSomeGovFiles());
        }
    }

    public boolean sendAllMediaFocus() {
        return updateInfo(crawlerService.getAllMediaFocus());
    }

    public boolean sendSomeMediaFocus() {
        NewsResponse response = getLastPolicy();
        if (response == null) {
            log.error("获取媒体聚焦历史数据失败！");
            return false;
        }
        if (response.getIsDeleted().equals("1") && response.getNewsSrcAnnTime() == null) {
            return updateInfo(crawlerService.getSomeMediaFocus());
        } else {
            return updateInfo(filterFocus(response.getNewsSrcAnnTime(),crawlerService.getSomeMediaFocus()));
        }
    }

    private boolean updateInfo(List<NewAndSpecSaveBo> specialCase) {
        // !todo
        // 保存数据到lowBCode
        return false;
    }

    private NewsResponse getLastPolicy() {
        // !todo
        // 获取数据
        return new NewsResponse();
    }

    private NewsResponse getLastAlliance() {
        // !todo
        // 获取数据
        return new NewsResponse();
    }

    private NewsResponse getLastSpecialCase() {
        // !todo
        // 获取数据
        return new NewsResponse();
    }

    private NewsResponse getLastMediaFocus() {
        // !todo
        // 获取数据
        return new NewsResponse();
    }

    private List<NewAndSpecSaveBo> filterSpecialCase(String oldTime, List<NewAndSpecSaveBo> specialCase) {
        List<NewAndSpecSaveBo> filteredList = getFilteredList(oldTime, specialCase,1);
        filteredList.forEach(a -> a.getUpdate().setSpecialCaseListChart(
                ImageUtil.sendImageToLowCode(a.getUpdate().getSpecialCaseListChart())));
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
                ImageUtil.sendImageToLowCode(a.getUpdate().getSpecialCaseListChart())));
        return filteredList;
    }

    private List<NewAndSpecSaveBo> filterFiles(String oldTime, List<NewAndSpecSaveBo> filesList) {
        return getFilteredList(oldTime, filesList,3);
    }

    private List<NewAndSpecSaveBo> filterFocus(String oldTime, List<NewAndSpecSaveBo> fucosList) {
        return getFilteredList(oldTime, fucosList,4);
    }
}

