//
//  Cuw_PubOrderAnnotation.m
//  BJPinChe
//
//  Created by yc on 14-12-12.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import "Cus_PubOrderAnnotation.h"

@implementation Cus_PubOrderAnnotation
@synthesize coordinate;
@synthesize title;
@synthesize subtitle;
@synthesize orderIndex;
@synthesize orderText;

- (id)initWithLocation:(CLLocationCoordinate2D)coord {
    self = [super init];
    if (self) {
        coordinate = coord;
        
    }
    return self;
}

- (void)setCoordinate:(CLLocationCoordinate2D)newCoordinate {
    coordinate = newCoordinate;
}
@end
