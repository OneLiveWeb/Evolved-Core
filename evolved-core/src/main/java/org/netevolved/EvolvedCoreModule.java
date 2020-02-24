package org.netevolved;

import org.json.simple.JSONObject;
import org.netevolved.sync.EmUploader;
import org.openedit.OpenEditException;
import org.openedit.WebPageRequest;
import org.openedit.modules.BaseModule;
import org.openedit.page.Page;

import java.util.ArrayList;
import java.util.List;

public class EvolvedCoreModule extends BaseModule {


    public EvolvedCore loadCore(WebPageRequest inReq) {


        String catalogid = inReq.findValue("catalogid");
        String applicationid = inReq.findValue("applicationid");
        if (applicationid == null) {
            applicationid = inReq.findValue("applicationid");
        }

        EvolvedCore core = getEvolvedCore(catalogid);
        inReq.putPageValue("evolvedcore", core);
        inReq.putPageValue("core", core);

        inReq.putPageValue("apphome", "/" + applicationid);
        inReq.putPageValue("applicationid", applicationid);

        inReq.putPageValue("catalogid", catalogid);

        return core;

    }

    public EvolvedCore getEvolvedCore(String inCatalogid) {
        EvolvedCore core = (EvolvedCore) getModuleManager().getBean(inCatalogid, "evolvedCore");

        return core;
    }

    public EvolvedCore getEvolvedCore(WebPageRequest inReq) {
        return loadCore(inReq);


    }


    public void uploadImages(WebPageRequest inReq) {


        EvolvedCore core = getEvolvedCore(inReq);
        List<String> pages = core.getPageManager().getChildrenPathsSorted("/evolved/photos");

        for (String page : pages) {
            Page downloaded = getEvolvedCore(inReq).getPageManager().getPage(page);


            JSONObject details = new JSONObject();
            try {
                details.put("name", "Ian was here");
                details.put("mediadb", "construction/mediadb");
                details.put("catalogid", "construction/catalog");
                details.put("categoryid", "AW_4ZcIwdsmzaSdV6b8E");

            } catch (Exception e) {
                throw new OpenEditException(e);
            }

            EmUploader uploader = new EmUploader();
            uploader.uploadAsset("adminmd5421c0af185908a6c0c40d50fd5e3f16760d5580bc", "http://home.netevolved.com:9101/", downloaded, details);
        }
    }


}
