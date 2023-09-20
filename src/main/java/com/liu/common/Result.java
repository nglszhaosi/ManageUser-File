package com.liu.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *接口统一返回包装类
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private String code;//成功失败状态码
    private String msg;//给前端返回失败的原因
    private Object data;//给前端返回后台携带的数据

    public static Result success(){
        return new Result(Constants.CODE_200,"",null);
    }

    public static Result success(Object data){
        return new Result(Constants.CODE_200,"",data);
    }

    public static Result error(String code ,String msg){
        return new Result(code,msg,null);
    }

    public static Result error(){
        return new Result(Constants.CODE_500,"系统错误",null);
    }

}
