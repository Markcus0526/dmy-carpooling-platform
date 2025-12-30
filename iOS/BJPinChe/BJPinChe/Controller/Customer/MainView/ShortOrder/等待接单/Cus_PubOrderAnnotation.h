//
//  Cuw_PubOrderAnnotation.h
//  BJPinChe
//
//  Created by yc on 14-12-12.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <MapKit/MapKit.h>
#import "BMapKit.h"

@interface Cus_PubOrderAnnotation : NSObject<BMKAnnotation>
@property (nonatomic, readonly) CLLocationCoordinate2D coordinate;
@property (nonatomic, copy) NSString *title;
@property (nonatomic, copy) NSString *subtitle;
@property (nonatomic, assign) NSInteger orderIndex;
@property (nonatomic, copy) NSString *orderText;

- (id)initWithLocation:(CLLocationCoordinate2D)coord;
- (void)setCoordinate:(CLLocationCoordinate2D)newCoordinate;
@end
