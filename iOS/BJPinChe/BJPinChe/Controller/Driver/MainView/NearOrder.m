//
//  NearOrder.m
//  BJPinChe
//
//  Created by yc on 15-1-30.
//  Copyright (c) 2015年 KimOC. All rights reserved.
//

#import "NearOrder.h"

@implementation NearOrder
- (id)init {
    @synchronized(self) {
        self = [super init];//往往放一些要初始化的变量.
        if (_geo_search ==nil) {
        }
        return self;
    }
}
-(void)nearCustomerOrder:(NSDictionary *)dic :(CLLocationCoordinate2D)location
{
    NSLog(@"======%@",dic);
    
    NSString *startLat = [dic objectForKey:@"startlat"];
    NSString *startLog = [dic objectForKey:@"startlog"];

    NSString *endLat = [dic objectForKey:@"endlat"];
    NSString *endLog = [dic objectForKey:@"endlog"];
    
    double distance = [self distanceBetweenOrderBy:[startLat doubleValue] :[endLat doubleValue] :[startLog doubleValue] :[endLog doubleValue]];
    
    double distance1 = [self distanceBetweenOrderBy:[startLat doubleValue] :location.latitude :[startLog doubleValue] :location.longitude];
    
    NSString *disStr =nil;
    if (distance1 < 1) {
        distance1 = distance1*1000;
        disStr = [NSString stringWithFormat:@"<%0.fm",distance1];
    }else
    {
        disStr = [NSString stringWithFormat:@"%0.2fKM",distance1];
    }
    if (distance >2 && distance <35) {
        NSString *strDis = [NSString stringWithFormat:@"%0.2f",distance];
        NSString *price = [NSString stringWithFormat:@"%0.2f",[strDis doubleValue]*2.5];
        NSString *sysprice = [NSString stringWithFormat:@"%0.2f",[price doubleValue]/10];
        NSMutableDictionary *order = [NSMutableDictionary dictionary];
        [order setValue:strDis forKey:@"mileage"];//总距离
        [order setObject:disStr forKey:@"distance_desc"];//距离乘客
        [order setObject:[dic objectForKey:@"startAddress"] forKey:@"start_addr"];//出发地
        [order setObject:[dic objectForKey:@"endAddress"] forKey:@"end_addr"];//目的地
        [order setObject:[self startTimer] forKey:@"start_time"];//出发时间
        [order setObject:price forKey:@"price"];//订单价格
        [order setObject:sysprice forKey:@"sysinfo_fee"];//平台费
        [order setObject:@"0" forKey:@"insun_fee"];//保险费
        [order setObject:@"假" forKey:@"false"];//假订单标示
        [order setObject:@"2" forKey:@"status"];//可以抢的假订单
        [order setObject:startLat forKey:@"startlat"];
        [order setObject:startLog forKey:@"startlog"];
        [order setObject:endLat forKey:@"endlat"];
        [order setObject:endLog forKey:@"endlog"];
        
        int eval = arc4random()%10+90;
        NSString *str =@"％";
        NSString *evalStr = [NSString stringWithFormat:@"%d%@",eval,str];
        [order setObject:evalStr forKey:@"EvalPro"];//好评率
        
        int serve = arc4random()%20 +1 ;
        NSString *serveStr = [NSString stringWithFormat:@"%d",serve];
        [order setObject:serveStr forKey:@"ServeCnt"];//拼车次数
        
        
        int age = arc4random()%10 +20;
        NSString *strAge = [NSString stringWithFormat:@"%d",age];
        [order setObject:strAge forKey:@"pass_age"];
        [order setObject:@"" forKey:@"pass_img"];
        [order setObject:@"" forKey:@"order_num"];
        NSArray *array = [NSArray arrayWithObjects:@"哈哈", @"abc", @"圈圈" ,@"超" ,@"鹏", @"小吕", @"王", @"qwe", @"qaz", @"嘿嘿", @"OO", @"xx", @"哈呵",nil];
        int name = arc4random()%13;
        [order setObject:[array objectAtIndex:name] forKey:@"pass_name"];
        
        [self fieldPlist:order];
    }
//    STSingleTimeOrderInfo *stOrder = [[STSingleTimeOrderInfo alloc] init];
//    stOrder.uid = [[item objectForKey:@"uid"] longValue];
//    stOrder.order_num = [item objectForKey:@"order_num"];
//    stOrder.pass_id = [[item objectForKey:@"pass_id"] intValue];
//    stOrder.image = [item objectForKey:@"pass_img"];
//    stOrder.name = [item objectForKey:@"pass_name"];
//    stOrder.sex = [[item objectForKey:@"pass_gender"] intValue];
//    stOrder.age = [[item objectForKey:@"pass_age"] intValue];
//    stOrder.price = [[item objectForKey:@"price"] doubleValue];
//    stOrder.sysinfo_fee = [[item objectForKey:@"sysinfo_fee"] doubleValue];
//    stOrder.sysinfo_fee_desc = [item objectForKey:@"sysinfo_fee_desc"];
//    stOrder.insun_fee =[[item objectForKey:@"insun_fee"]doubleValue];
//    stOrder.startPos = [item objectForKey:@"start_addr"];
//    stOrder.endPos = [item objectForKey:@"end_addr"];
//    stOrder.distance = [[item objectForKey:@"distance"] doubleValue];
//    stOrder.distance_desc = [item objectForKey:@"distance_desc"];
//    stOrder.midpoints = [[item objectForKey:@"midpoints"] intValue];
//    stOrder.midpoints_desc = [item objectForKey:@"midpoints_desc"];
//    stOrder.start_time = [item objectForKey:@"start_time"];
//    stOrder.create_time = [item objectForKey:@"create_time"];
//    stOrder.status = [item objectForKey:@"status"];
//    stOrder.mileage = [[item objectForKey:@"mileage"] doubleValue];
//    [dataList addObject:stOrder];
}
-(double)distanceBetweenOrderBy:(double)lat1 :(double)lat2 :(double)lng1 :(double)lng2{
    double dd = M_PI/180;
    double x1=lat1*dd,x2=lat2*dd;
    double y1=lng1*dd,y2=lng2*dd;
    double R = 6371004;
    double distance = (2*R*asin(sqrt(2-2*cos(x1)*cos(x2)*cos(y1-y2) - 2*sin(x1)*sin(x2))/2));
    
    return   distance/1000;
    
}
-(NSString *)startTimer
{
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy-MM-dd HH:mm"];
    int rlat = arc4random()%10;
    int start = arc4random()%20+rlat +1;
    NSString *destDateString = [dateFormatter stringFromDate:[[NSDate date] dateByAddingTimeInterval:(start * 60)]];
    return destDateString;
}
-(void)fieldPlist:(NSDictionary *)orderDic
{
    NSMutableArray *resultData = [[NSMutableArray alloc]init];
    NSArray *paths=NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,NSUserDomainMask,YES);
    NSString *path=[paths objectAtIndex:0];
    NSString *filename=[path stringByAppendingPathComponent:@"orderdes.plist"];
    
    //读文件
    NSDictionary* dic2 = [NSDictionary dictionaryWithContentsOfFile:filename];
    
    if(dic2 == nil)
    {
        //1. 创建一个plist文件
        NSFileManager* fm = [NSFileManager defaultManager];
        [fm createFileAtPath:filename contents:nil attributes:nil];
        
        [resultData addObject:orderDic];
        NSDictionary *dic =[NSDictionary dictionaryWithObject:resultData forKey:@"order"];
        [dic writeToFile:filename atomically:YES];
    }
    else
    {
        resultData=[dic2 objectForKey:@"order"];
        for (int i=0 ; i< resultData.count; i++) {
            NSDictionary *dic = [resultData objectAtIndex:i];
            NSString *timer = [dic objectForKey:@"start_time"];
            if ([self nowTimer:timer]) {
                [resultData removeObjectAtIndex:i];
            }
        }
        [resultData addObject:orderDic];
        NSDictionary *dic =[NSDictionary dictionaryWithObject:resultData forKey:@"order"];
        [dic writeToFile:filename atomically:YES];
    }
}
-(BOOL)nowTimer:(NSString *)timer;
{
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:@"yyyy-MM-dd HH:mm"];
    
    NSDate* selDate = [formatter dateFromString:timer];
    NSDate* curDate = [NSDate date];
    
    if ([curDate timeIntervalSince1970] > [selDate timeIntervalSince1970])
    {
        NSLog(@"时间过了。。。");
        return YES;
    }else
    {
        NSLog(@"还好。。。");
        return NO;
    }
}
@end
