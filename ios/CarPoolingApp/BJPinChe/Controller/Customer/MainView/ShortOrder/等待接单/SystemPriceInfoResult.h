//
//  SystemPriceInfoResult.h
//  BJPinChe
//
//  Created by APP_USER on 14-10-29.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

/**
 *
 car_style : 车型(int)  1:经济型,2:舒适性,3:豪华型,4:商务型,
 aver_price : 平均价(double),
 price_interval : 平台推荐价加价幅度(double),
 min_limit : 平台最低限价(double),
 distance : 距离(double),
 description : 描述(String)
 */

#import <Foundation/Foundation.h>

@interface SystemPriceInfoResult : NSObject

@property(nonatomic,copy)NSString *system_description;
@property (nonatomic,assign)NSInteger car_style;
@property(nonatomic,assign)double price_interval;
@property(nonatomic,assign)double distance;
@property(nonatomic,assign)double aver_price;
@property(nonatomic,assign)double min_limit;


+(instancetype)systemPriceInfoResultWithDict:(NSDictionary *)dict;
-(instancetype)initWithDict:(NSDictionary *)dict;

@end
