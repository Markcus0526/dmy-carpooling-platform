//
//  OOAnnotation.m
//  BJPinChe
//
//  Created by APP_USER on 14-11-26.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import "OOAnnotation.h"


@implementation OOAnnotation
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

