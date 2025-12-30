//
//  NearOrder.h
//  BJPinChe
//
//  Created by yc on 15-1-30.
//  Copyright (c) 2015年 KimOC. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "BMapKit.h"
@interface NearOrder : NSObject<BMKGeoCodeSearchDelegate,BMKMapViewDelegate>
@property(nonatomic,strong) BMKGeoCodeSearch *geo_search;
-(void)nearCustomerOrder:(NSDictionary *)dic :(CLLocationCoordinate2D)location;
@end
