package data.service;

import data.dto.SawonDto;
import data.mapper.SawonMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor //전체생성자 주입
public class SawonService {

    private SawonMapper sawonMapper;

    public void insertSawon(SawonDto dto){
        sawonMapper.insertSawon(dto);
    }

    public List<SawonDto> getSelectAllSawon(){
        return sawonMapper.getSelectAllSawon();
    }

    public SawonDto getSawon(int num){
        return sawonMapper.getSawon(num);
    }

    public void deleteSawon(int num){
        sawonMapper.deleteSawon(num);
    }

    public void updateSawon(SawonDto dto){
        sawonMapper.updateSawon(dto);
    }

}
