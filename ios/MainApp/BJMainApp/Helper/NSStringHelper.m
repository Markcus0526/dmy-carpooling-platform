//
//  NSStringHelper.m
//  BodyWear
//
//  Created by RyuCJ on 8/27/13.
//  Copyright (c) 2013 damytech. All rights reserved.
//

#import "NSStringHelper.h"

@implementation NSString (trimeLeadingWhitespace)

-(NSString*)stringByTrimmingLeadingWhitespace;
{
    NSInteger i = 0;
    while ((i < [self length])
           && [[NSCharacterSet whitespaceCharacterSet] characterIsMember:[self characterAtIndex:i]])
    {
        i++;
    }
    return [self substringFromIndex:i];
}

@end
