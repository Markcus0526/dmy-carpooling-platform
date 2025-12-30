//
//  OOAnnotation.h
//  BJPinChe
//
//  Created by APP_USER on 14-11-26.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "BMapKit.h"
@interface OOAnnotation : NSObject <BMKAnnotation>
@property (nonatomic, readonly) CLLocationCoordinate2D coordinate;
@property (nonatomic, copy) NSString *title;
@property (nonatomic, copy) NSString *subtitle;
@property (nonatomic, assign) NSInteger orderIndex;
@property (nonatomic, copy) NSString *orderText;

- (id)initWithLocation:(CLLocationCoordinate2D)coord;
- (void)setCoordinate:(CLLocationCoordinate2D)newCoordinate;
@end
