package com.mahao.customview.okhttp;

import java.util.List;


public class DateBean {

    public int showapi_res_code;
    public String showapi_res_error;
    public ShowapiResBodyDTO showapi_res_body;

    public int getShowapi_res_code() {
        return showapi_res_code;
    }

    public void setShowapi_res_code(int showapi_res_code) {
        this.showapi_res_code = showapi_res_code;
    }

    public String getShowapi_res_error() {
        return showapi_res_error;
    }

    public void setShowapi_res_error(String showapi_res_error) {
        this.showapi_res_error = showapi_res_error;
    }

    public ShowapiResBodyDTO getShowapi_res_body() {
        return showapi_res_body;
    }

    public void setShowapi_res_body(ShowapiResBodyDTO showapi_res_body) {
        this.showapi_res_body = showapi_res_body;
    }

    public static class ShowapiResBodyDTO {
        public F6DTO f6;
        public String date;
        public int ret_code;
        public String remark;
        public CityInfoDTO cityInfo;

        public F6DTO getF6() {
            return f6;
        }

        public void setF6(F6DTO f6) {
            this.f6 = f6;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getRet_code() {
            return ret_code;
        }

        public void setRet_code(int ret_code) {
            this.ret_code = ret_code;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public CityInfoDTO getCityInfo() {
            return cityInfo;
        }

        public void setCityInfo(CityInfoDTO cityInfo) {
            this.cityInfo = cityInfo;
        }

        public static class F6DTO {
            public String day_weather;
            public String night_weather;
            public String night_weather_code;
            public IndexDTO index;
            public String night_wind_power;
            public String day_wind_power;
            public String day_weather_code;
            public String sun_begin_end;
            public String day_weather_pic;
            public int weekday;
            public String night_air_temperature;
            public String day_air_temperature;
            public String day_wind_direction;
            public String day;
            public String night_weather_pic;
            public String night_wind_direction;
            @com.google.gson.annotations.SerializedName("3hourForcast")
            public List<_$3hourForcastDTO> _$3hourForcast;

            public String getDay_weather() {
                return day_weather;
            }

            public void setDay_weather(String day_weather) {
                this.day_weather = day_weather;
            }

            public String getNight_weather() {
                return night_weather;
            }

            public void setNight_weather(String night_weather) {
                this.night_weather = night_weather;
            }

            public String getNight_weather_code() {
                return night_weather_code;
            }

            public void setNight_weather_code(String night_weather_code) {
                this.night_weather_code = night_weather_code;
            }

            public IndexDTO getIndex() {
                return index;
            }

            public void setIndex(IndexDTO index) {
                this.index = index;
            }

            public String getNight_wind_power() {
                return night_wind_power;
            }

            public void setNight_wind_power(String night_wind_power) {
                this.night_wind_power = night_wind_power;
            }

            public String getDay_wind_power() {
                return day_wind_power;
            }

            public void setDay_wind_power(String day_wind_power) {
                this.day_wind_power = day_wind_power;
            }

            public String getDay_weather_code() {
                return day_weather_code;
            }

            public void setDay_weather_code(String day_weather_code) {
                this.day_weather_code = day_weather_code;
            }

            public String getSun_begin_end() {
                return sun_begin_end;
            }

            public void setSun_begin_end(String sun_begin_end) {
                this.sun_begin_end = sun_begin_end;
            }

            public String getDay_weather_pic() {
                return day_weather_pic;
            }

            public void setDay_weather_pic(String day_weather_pic) {
                this.day_weather_pic = day_weather_pic;
            }

            public int getWeekday() {
                return weekday;
            }

            public void setWeekday(int weekday) {
                this.weekday = weekday;
            }

            public String getNight_air_temperature() {
                return night_air_temperature;
            }

            public void setNight_air_temperature(String night_air_temperature) {
                this.night_air_temperature = night_air_temperature;
            }

            public String getDay_air_temperature() {
                return day_air_temperature;
            }

            public void setDay_air_temperature(String day_air_temperature) {
                this.day_air_temperature = day_air_temperature;
            }

            public String getDay_wind_direction() {
                return day_wind_direction;
            }

            public void setDay_wind_direction(String day_wind_direction) {
                this.day_wind_direction = day_wind_direction;
            }

            public String getDay() {
                return day;
            }

            public void setDay(String day) {
                this.day = day;
            }

            public String getNight_weather_pic() {
                return night_weather_pic;
            }

            public void setNight_weather_pic(String night_weather_pic) {
                this.night_weather_pic = night_weather_pic;
            }

            public String getNight_wind_direction() {
                return night_wind_direction;
            }

            public void setNight_wind_direction(String night_wind_direction) {
                this.night_wind_direction = night_wind_direction;
            }

            public List<_$3hourForcastDTO> get_$3hourForcast() {
                return _$3hourForcast;
            }

            public void set_$3hourForcast(List<_$3hourForcastDTO> _$3hourForcast) {
                this._$3hourForcast = _$3hourForcast;
            }

            public static class IndexDTO {
                public YhDTO yh;
                public LsDTO ls;
                public ClothesDTO clothes;
                public DyDTO dy;
                public BeautyDTO beauty;
                public XqDTO xq;
                public TravelDTO travel;
                public HcDTO hc;
                public ZsDTO zs;
                public ColdDTO cold;
                public GjDTO gj;
                public UvDTO uv;
                public ClDTO cl;
                public GlassDTO glass;
                public WashCarDTO wash_car;
                public AqiDTO aqi;
                public AcDTO ac;
                public MfDTO mf;
                public AgDTO ag;
                public PjDTO pj;
                public NlDTO nl;
                public PkDTO pk;

                public YhDTO getYh() {
                    return yh;
                }

                public void setYh(YhDTO yh) {
                    this.yh = yh;
                }

                public LsDTO getLs() {
                    return ls;
                }

                public void setLs(LsDTO ls) {
                    this.ls = ls;
                }

                public ClothesDTO getClothes() {
                    return clothes;
                }

                public void setClothes(ClothesDTO clothes) {
                    this.clothes = clothes;
                }

                public DyDTO getDy() {
                    return dy;
                }

                public void setDy(DyDTO dy) {
                    this.dy = dy;
                }

                public BeautyDTO getBeauty() {
                    return beauty;
                }

                public void setBeauty(BeautyDTO beauty) {
                    this.beauty = beauty;
                }

                public XqDTO getXq() {
                    return xq;
                }

                public void setXq(XqDTO xq) {
                    this.xq = xq;
                }

                public TravelDTO getTravel() {
                    return travel;
                }

                public void setTravel(TravelDTO travel) {
                    this.travel = travel;
                }

                public HcDTO getHc() {
                    return hc;
                }

                public void setHc(HcDTO hc) {
                    this.hc = hc;
                }

                public ZsDTO getZs() {
                    return zs;
                }

                public void setZs(ZsDTO zs) {
                    this.zs = zs;
                }

                public ColdDTO getCold() {
                    return cold;
                }

                public void setCold(ColdDTO cold) {
                    this.cold = cold;
                }

                public GjDTO getGj() {
                    return gj;
                }

                public void setGj(GjDTO gj) {
                    this.gj = gj;
                }

                public UvDTO getUv() {
                    return uv;
                }

                public void setUv(UvDTO uv) {
                    this.uv = uv;
                }

                public ClDTO getCl() {
                    return cl;
                }

                public void setCl(ClDTO cl) {
                    this.cl = cl;
                }

                public GlassDTO getGlass() {
                    return glass;
                }

                public void setGlass(GlassDTO glass) {
                    this.glass = glass;
                }

                public WashCarDTO getWash_car() {
                    return wash_car;
                }

                public void setWash_car(WashCarDTO wash_car) {
                    this.wash_car = wash_car;
                }

                public AqiDTO getAqi() {
                    return aqi;
                }

                public void setAqi(AqiDTO aqi) {
                    this.aqi = aqi;
                }

                public AcDTO getAc() {
                    return ac;
                }

                public void setAc(AcDTO ac) {
                    this.ac = ac;
                }

                public MfDTO getMf() {
                    return mf;
                }

                public void setMf(MfDTO mf) {
                    this.mf = mf;
                }

                public AgDTO getAg() {
                    return ag;
                }

                public void setAg(AgDTO ag) {
                    this.ag = ag;
                }

                public PjDTO getPj() {
                    return pj;
                }

                public void setPj(PjDTO pj) {
                    this.pj = pj;
                }

                public NlDTO getNl() {
                    return nl;
                }

                public void setNl(NlDTO nl) {
                    this.nl = nl;
                }

                public PkDTO getPk() {
                    return pk;
                }

                public void setPk(PkDTO pk) {
                    this.pk = pk;
                }

                public static class YhDTO {
                    public String title;
                    public String desc;

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }

                    public String getDesc() {
                        return desc;
                    }

                    public void setDesc(String desc) {
                        this.desc = desc;
                    }
                }

                public static class LsDTO {
                    public String title;
                    public String desc;

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }

                    public String getDesc() {
                        return desc;
                    }

                    public void setDesc(String desc) {
                        this.desc = desc;
                    }
                }

                public static class ClothesDTO {
                    public String title;
                    public String desc;

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }

                    public String getDesc() {
                        return desc;
                    }

                    public void setDesc(String desc) {
                        this.desc = desc;
                    }
                }

                public static class DyDTO {
                    public String title;
                    public String desc;

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }

                    public String getDesc() {
                        return desc;
                    }

                    public void setDesc(String desc) {
                        this.desc = desc;
                    }
                }

                public static class BeautyDTO {
                    public String title;
                    public String desc;

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }

                    public String getDesc() {
                        return desc;
                    }

                    public void setDesc(String desc) {
                        this.desc = desc;
                    }
                }

                public static class XqDTO {
                    public String title;
                    public String desc;

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }

                    public String getDesc() {
                        return desc;
                    }

                    public void setDesc(String desc) {
                        this.desc = desc;
                    }
                }

                public static class TravelDTO {
                    public String title;
                    public String desc;

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }

                    public String getDesc() {
                        return desc;
                    }

                    public void setDesc(String desc) {
                        this.desc = desc;
                    }
                }

                public static class HcDTO {
                    public String title;
                    public String desc;

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }

                    public String getDesc() {
                        return desc;
                    }

                    public void setDesc(String desc) {
                        this.desc = desc;
                    }
                }

                public static class ZsDTO {
                    public String title;
                    public String desc;

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }

                    public String getDesc() {
                        return desc;
                    }

                    public void setDesc(String desc) {
                        this.desc = desc;
                    }
                }

                public static class ColdDTO {
                    public String title;
                    public String desc;

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }

                    public String getDesc() {
                        return desc;
                    }

                    public void setDesc(String desc) {
                        this.desc = desc;
                    }
                }

                public static class GjDTO {
                    public String title;
                    public String desc;

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }

                    public String getDesc() {
                        return desc;
                    }

                    public void setDesc(String desc) {
                        this.desc = desc;
                    }
                }

                public static class UvDTO {
                    public String title;
                    public String desc;

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }

                    public String getDesc() {
                        return desc;
                    }

                    public void setDesc(String desc) {
                        this.desc = desc;
                    }
                }

                public static class ClDTO {
                    public String title;
                    public String desc;

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }

                    public String getDesc() {
                        return desc;
                    }

                    public void setDesc(String desc) {
                        this.desc = desc;
                    }
                }

                public static class GlassDTO {
                    public String title;
                    public String desc;

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }

                    public String getDesc() {
                        return desc;
                    }

                    public void setDesc(String desc) {
                        this.desc = desc;
                    }
                }

                public static class WashCarDTO {
                    public String title;
                    public String desc;

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }

                    public String getDesc() {
                        return desc;
                    }

                    public void setDesc(String desc) {
                        this.desc = desc;
                    }
                }

                public static class AqiDTO {
                    public String title;
                    public String desc;

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }

                    public String getDesc() {
                        return desc;
                    }

                    public void setDesc(String desc) {
                        this.desc = desc;
                    }
                }

                public static class AcDTO {
                    public String title;
                    public String desc;

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }

                    public String getDesc() {
                        return desc;
                    }

                    public void setDesc(String desc) {
                        this.desc = desc;
                    }
                }

                public static class MfDTO {
                    public String title;
                    public String desc;

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }

                    public String getDesc() {
                        return desc;
                    }

                    public void setDesc(String desc) {
                        this.desc = desc;
                    }
                }

                public static class AgDTO {
                    public String title;
                    public String desc;

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }

                    public String getDesc() {
                        return desc;
                    }

                    public void setDesc(String desc) {
                        this.desc = desc;
                    }
                }

                public static class PjDTO {
                    public String title;
                    public String desc;

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }

                    public String getDesc() {
                        return desc;
                    }

                    public void setDesc(String desc) {
                        this.desc = desc;
                    }
                }

                public static class NlDTO {
                    public String title;
                    public String desc;

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }

                    public String getDesc() {
                        return desc;
                    }

                    public void setDesc(String desc) {
                        this.desc = desc;
                    }
                }

                public static class PkDTO {
                    public String title;
                    public String desc;

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }

                    public String getDesc() {
                        return desc;
                    }

                    public void setDesc(String desc) {
                        this.desc = desc;
                    }
                }
            }

            public static class _$3hourForcastDTO {
                public String hour;
                public String temperature;
                public String weather;
                public String weather_pic;
                public String wind_direction;
                public String wind_power;

                public String getHour() {
                    return hour;
                }

                public void setHour(String hour) {
                    this.hour = hour;
                }

                public String getTemperature() {
                    return temperature;
                }

                public void setTemperature(String temperature) {
                    this.temperature = temperature;
                }

                public String getWeather() {
                    return weather;
                }

                public void setWeather(String weather) {
                    this.weather = weather;
                }

                public String getWeather_pic() {
                    return weather_pic;
                }

                public void setWeather_pic(String weather_pic) {
                    this.weather_pic = weather_pic;
                }

                public String getWind_direction() {
                    return wind_direction;
                }

                public void setWind_direction(String wind_direction) {
                    this.wind_direction = wind_direction;
                }

                public String getWind_power() {
                    return wind_power;
                }

                public void setWind_power(String wind_power) {
                    this.wind_power = wind_power;
                }
            }
        }

        public static class CityInfoDTO {
            public String c6;
            public String c5;
            public String c4;
            public String c3;
            public String c9;
            public String c8;
            public String c7;
            public String c17;
            public String c16;
            public String c1;
            public String c2;
            public String c11;
            public double longitude;
            public String c10;
            public double latitude;
            public String c12;
            public String c15;

            public String getC6() {
                return c6;
            }

            public void setC6(String c6) {
                this.c6 = c6;
            }

            public String getC5() {
                return c5;
            }

            public void setC5(String c5) {
                this.c5 = c5;
            }

            public String getC4() {
                return c4;
            }

            public void setC4(String c4) {
                this.c4 = c4;
            }

            public String getC3() {
                return c3;
            }

            public void setC3(String c3) {
                this.c3 = c3;
            }

            public String getC9() {
                return c9;
            }

            public void setC9(String c9) {
                this.c9 = c9;
            }

            public String getC8() {
                return c8;
            }

            public void setC8(String c8) {
                this.c8 = c8;
            }

            public String getC7() {
                return c7;
            }

            public void setC7(String c7) {
                this.c7 = c7;
            }

            public String getC17() {
                return c17;
            }

            public void setC17(String c17) {
                this.c17 = c17;
            }

            public String getC16() {
                return c16;
            }

            public void setC16(String c16) {
                this.c16 = c16;
            }

            public String getC1() {
                return c1;
            }

            public void setC1(String c1) {
                this.c1 = c1;
            }

            public String getC2() {
                return c2;
            }

            public void setC2(String c2) {
                this.c2 = c2;
            }

            public String getC11() {
                return c11;
            }

            public void setC11(String c11) {
                this.c11 = c11;
            }

            public double getLongitude() {
                return longitude;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            public String getC10() {
                return c10;
            }

            public void setC10(String c10) {
                this.c10 = c10;
            }

            public double getLatitude() {
                return latitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public String getC12() {
                return c12;
            }

            public void setC12(String c12) {
                this.c12 = c12;
            }

            public String getC15() {
                return c15;
            }

            public void setC15(String c15) {
                this.c15 = c15;
            }

            @Override
            public String toString() {
                return "CityInfoDTO{" +
                        "c6='" + c6 + '\'' +
                        ", c5='" + c5 + '\'' +
                        ", c4='" + c4 + '\'' +
                        ", c3='" + c3 + '\'' +
                        ", c9='" + c9 + '\'' +
                        ", c8='" + c8 + '\'' +
                        ", c7='" + c7 + '\'' +
                        ", c17='" + c17 + '\'' +
                        ", c16='" + c16 + '\'' +
                        ", c1='" + c1 + '\'' +
                        ", c2='" + c2 + '\'' +
                        ", c11='" + c11 + '\'' +
                        ", longitude=" + longitude +
                        ", c10='" + c10 + '\'' +
                        ", latitude=" + latitude +
                        ", c12='" + c12 + '\'' +
                        ", c15='" + c15 + '\'' +
                        '}';
            }
        }
    }
}
