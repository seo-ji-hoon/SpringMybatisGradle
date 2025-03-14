package data.controller;

import data.dto.SawonDto;
import data.service.SawonService;
import lombok.RequiredArgsConstructor;
import naver.storage.NcpObjectStorageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Vector;

@Controller
@RequiredArgsConstructor
public class SawonController {

    final SawonService sawonService;
    final NcpObjectStorageService storageService;

    private String imagePath="https://kr.object.ncloudstorage.com/bitcamp-bucket-110/sawon/";
    private String bucketName="bitcamp-bucket-110";

    @GetMapping("/")
    public String mainpage()
    {
        return "sawon/mainpage";
    }

    @GetMapping("/list")
    public String sawonList(Model model)
    {
        List<SawonDto> list=sawonService.getSelectAllSawon();

        model.addAttribute("list",list);
        model.addAttribute("totalCount",list.size());
        model.addAttribute("imagePath",imagePath);

        return "sawon/sawonlist";
    }

    @GetMapping("/form")
    public String sawonForm() {

        return "sawon/sawonform";
    }

    @PostMapping("/insert")
    public String sawonList(@ModelAttribute SawonDto dto, @RequestParam("upload")MultipartFile upload){

        if(upload.getOriginalFilename().equals("")){
            dto.setPhoto(null);
        }else {
            String photo = storageService.uploadFile(bucketName,"sawon",upload);
            dto.setPhoto(photo);
        }

        sawonService.insertSawon(dto);

        return "redirect:./list";
    }

    @GetMapping("/detail")
    public String sawonDetail(@RequestParam("num") int num,Model model){

        SawonDto dto = sawonService.getSawon(num);

        // 모델에 저장
        model.addAttribute("dto", dto);
        model.addAttribute("imagePath",imagePath);
        model.addAttribute("naverurl", "https://kr.object.ncloudstorage.com/" + bucketName);

        return"sawon/sawondetail";
    }

    @GetMapping("/delete")
    public String sawonDelete(@RequestParam("num") int num) {

        //저장된 사진 지우기
        String photo = sawonService.getSawon(num).getPhoto();
        storageService.deleteFile(bucketName,"sawon",photo);

        //db 데이타 삭제
        sawonService.deleteSawon(num);

        return "redirect:./list";
    }

    @GetMapping("/updateform")
    public String sawonUpdateForm(@RequestParam("num") int num,Model model){

        SawonDto dto = sawonService.getSawon(num);

        model.addAttribute("dto", dto);
        model.addAttribute("imagePath",imagePath);

        return "sawon/sawonupdate";
    }

    //수정할 때 실행되는 컨트롤러
    @PostMapping("/update")
    public String sawonUpdate(@ModelAttribute SawonDto dto,
                              @RequestParam("upload") MultipartFile upload) {
        if(upload.getOriginalFilename().equals("")) {
            dto.setPhoto(null);
        }
        else {
            String photo=storageService.uploadFile(bucketName, "sawon", upload);
            dto.setPhoto(photo);
        }
        sawonService.updateSawon(dto);

        return "redirect:./detail?num="+dto.getNum();
    }

}


